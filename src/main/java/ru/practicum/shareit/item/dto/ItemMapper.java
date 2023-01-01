package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner() != null ? String.valueOf(item.getOwner().getId()) : "null")
                .request(item.getRequest() != null ? String.valueOf(item.getRequest().getId()) : "null")
                .build();
    }
}
