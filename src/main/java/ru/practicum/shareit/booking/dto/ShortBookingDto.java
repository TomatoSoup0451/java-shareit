package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShortBookingDto {
    long id;
    long bookerId;
}
