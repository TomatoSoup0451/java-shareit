package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

@Builder
@Value
public class ItemDto {
    String name;
    String description;
    Boolean available;
    long owner;
    Long requestId;
    long id;
    ShortBookingDto lastBooking;
    ShortBookingDto nextBooking;
    List<CommentDto> comments;
}
