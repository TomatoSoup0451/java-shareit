package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Getter
public class ItemRequest {
    @Setter
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
