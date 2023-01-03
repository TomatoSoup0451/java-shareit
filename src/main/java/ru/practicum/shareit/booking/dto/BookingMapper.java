package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem().getId())
                .status(booking.getStatus().name())
                .build();
    }
}
