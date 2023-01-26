package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateRequestDto {
    long id;
    String description;
    long requestor;
}
