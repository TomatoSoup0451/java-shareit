package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto, User booker, Item item, Status status) {
        return Booking.builder()
                .booker(booker)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(status)
                .build();
    }

    public static ShortBookingDto toShortBookingDto(Booking booking) {
        return ShortBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
