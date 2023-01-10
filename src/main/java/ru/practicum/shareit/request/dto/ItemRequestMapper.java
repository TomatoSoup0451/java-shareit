package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .requestor(itemRequest.getRequestor().getId())
                .description(itemRequest.getDescription())
                .items(items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList()))
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor, LocalDateTime created){
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requestor(requestor)
                .created(created)
                .build();
    }
}
