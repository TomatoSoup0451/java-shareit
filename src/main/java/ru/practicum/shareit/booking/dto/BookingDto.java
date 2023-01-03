package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BookingDto {
    private final long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long item;
    private final long booker;
    private final String status;
}
