package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ItemRequestDto {
    private final Long id;
    private final String description;
    private final long requestor;
    private final LocalDateTime created;
}
