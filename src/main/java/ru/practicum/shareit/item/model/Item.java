package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
public class Item {
    @Setter
    private Long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final User owner;
    private final ItemRequest request;
}
