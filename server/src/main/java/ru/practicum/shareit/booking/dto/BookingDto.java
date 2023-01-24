package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.annotations.BookingDateValidation;

import java.time.LocalDateTime;

@Builder
@Value
@BookingDateValidation
public class BookingDto {
    long id;

    LocalDateTime start;

    LocalDateTime end;

    @JsonProperty("itemId")
    long item;

    long booker;

    String status;
}
