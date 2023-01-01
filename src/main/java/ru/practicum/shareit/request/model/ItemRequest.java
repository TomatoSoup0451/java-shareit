package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Builder
@Getter
public class ItemRequest {
    @Setter
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
