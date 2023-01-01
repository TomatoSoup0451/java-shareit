package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder
@Getter
public class Booking {
    @Setter
    private Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Item item;
    private final User booker;
    private final Status status;

    private enum Status{
        WAITING, APPROVED, REJECTED, CANCELED
    }
}
