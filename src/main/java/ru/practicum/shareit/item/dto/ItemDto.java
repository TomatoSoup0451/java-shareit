package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
public class ItemDto {
    @Setter
    private long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Long owner;
    private final Long request;
}
