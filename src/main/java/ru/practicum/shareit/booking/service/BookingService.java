package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, long bookerId);

    Booking updateStatus(long ownerId, long bookingId, boolean approved);

    Booking findBooking(long userId, long bookingId);

    List<Booking> getUserBookings(BookingState state, long bookerId);

    List<Booking> getOwnerItemsBookings(BookingState state, long ownerId);

    List<Booking> getUserBookings(BookingState state, long bookerId, Pageable pageable);

    List<Booking> getOwnerItemsBookings(BookingState state, long ownerId, Pageable pageable);
}

