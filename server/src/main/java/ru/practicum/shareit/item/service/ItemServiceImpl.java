package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + userId + " not found"));
        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId()).get();
        }
        Item newItem = ItemMapper.toItem(itemDto, owner, request);
        Item item = itemRepository.save(newItem);
        log.info("Item with id = {} added", item.getId());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IdNotFoundException("Item with id = " + itemId + " not found"));
        if (oldItem.getOwner().getId() != userId) {
            throw new ForbiddenException("Item with id = " + itemId +
                    " can't be modified by user with id = " + userId);
        }
        if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.save(oldItem);
        log.info("Item with id = {} updated", updatedItem.getId());
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItem(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IdNotFoundException("Item with id = " + itemId + " not found"));
        return (userId != item.getOwner().getId()) ? addCommentsToDto(ItemMapper.toItemDto(item)) :
                addCommentsToDto(getDtoWithBookings(item));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.findByOwnerIdOrderByIdAsc(userId).stream()
                .map(this::getDtoWithBookings)
                .map(this::addCommentsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByName(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findByName(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment addComment(long itemId, long userId, CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().isEmpty()) {
            throw new BadRequestException("Message in comment should not be empty");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IdNotFoundException("Item with id = " + itemId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException("User with id = " + userId + " not found"));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndBeforeOrderByIdDesc(userId, now);
        if (bookings.isEmpty()) {
            throw new BadRequestException("User with id = " + userId + " never used item with id = " + itemId);
        }
        return commentRepository.save(CommentMapper.toComment(commentDto, user, item, now));
    }

    @Override
    public List<ItemDto> getUserItems(long userId, Pageable pageable) {
        return itemRepository.findByOwnerId(userId, pageable).stream()
                .map(this::getDtoWithBookings)
                .map(this::addCommentsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByName(String text, Pageable pageable) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findByName(text, pageable).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private ItemDto getDtoWithBookings(Item item) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> lastBookings = bookingRepository.findByItemIdAndStartBeforeOrderByStartDesc(item.getId(), now);
        List<Booking> nextBookings = bookingRepository.findByItemIdAndStartAfterOrderByStartAsc(item.getId(), now);
        return ItemMapper.toItemDtoWithBookingDates(item,
                !lastBookings.isEmpty() ? lastBookings.get(0) : null,
                !nextBookings.isEmpty() ? nextBookings.get(0) : null);
    }

    private ItemDto addCommentsToDto(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findByItemId(itemDto.getId());
        if (comments != null && !comments.isEmpty()) {
            itemDto.getComments().addAll(comments.stream()
                    .map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }
        return itemDto;
    }
}
