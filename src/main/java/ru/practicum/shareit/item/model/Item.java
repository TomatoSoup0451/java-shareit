package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
@Setter
public class Item {
    private final User owner;
    private final ItemRequest request;
    private long id;
    private String name;
    private String description;
    private Boolean available;
}
