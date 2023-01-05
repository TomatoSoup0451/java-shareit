package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Builder
@Getter
public class Booking {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Item item;
    private final User booker;
    private final Status status;
    @Setter
    private long id;

    public enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }
}
