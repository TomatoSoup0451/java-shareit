package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addRequest(long requestorId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getUserRequests(long requestorId);

    ItemRequestDto getRequest(long requestorId, long requestId);

    List<ItemRequestDto> getAllRequests(Pageable pageable, long requestorId);

    List<ItemRequestDto> getAllRequests(long requestorId);
}
