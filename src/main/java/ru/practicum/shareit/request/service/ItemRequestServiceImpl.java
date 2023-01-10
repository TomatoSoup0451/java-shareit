package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest addRequest(long requestorId, ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            throw new BadRequestException("Message in request should not be empty");
        }
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto,
                userRepository.findById(requestorId)
                        .orElseThrow(() -> new IdNotFoundException("User with id = " + requestorId + " not found")),
                LocalDateTime.now()));
        log.info("Request with id = {} added", itemRequest.getId());
        return itemRequest;
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + requestorId + " not found"));
        return itemRequestRepository.findAllByRequestor(requestor).stream()
                .map(this::getItemRequestDtoWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(long requestId) {
        return getItemRequestDtoWithItems(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new IdNotFoundException("Request with id = " + requestId + " not found")));
    }

    private ItemRequestDto getItemRequestDtoWithItems(ItemRequest itemRequest) {
        return ItemRequestMapper.toItemRequestDto(itemRequest, itemRepository.findAllByRequest(itemRequest));
    }
}
