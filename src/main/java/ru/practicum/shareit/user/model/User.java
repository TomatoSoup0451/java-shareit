package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Getter
public class User {
    @Setter
    private long id;
    private final String name;
    private final String email;
}
