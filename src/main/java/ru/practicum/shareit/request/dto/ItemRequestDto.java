package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Value
public class ItemRequestDto {
    long id;
    String description;
    long requestor;
    List<ItemDto> items;
    LocalDateTime created;
}
