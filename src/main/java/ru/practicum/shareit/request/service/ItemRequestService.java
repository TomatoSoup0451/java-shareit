package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest addRequest(long requestorId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getUserRequests(long requestorId);
}
