package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class ItemRequestDto {
    Long id;
    String description;
    long requestor;
    LocalDateTime created;
}
