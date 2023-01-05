package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class BookingDto {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long item;
    long booker;
    String status;
}
