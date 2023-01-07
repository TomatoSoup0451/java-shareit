package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody @Valid BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") long bookerId,
                                 BindingResult errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping(path = "/{bookingId}")
    public Booking updateStatus(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.updateStatus(ownerId, bookingId, approved);
    }

    @GetMapping(path = "/{bookingId}")
    public Booking findBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getUsersBookings(@RequestParam(required = false) String state,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUserBookings(parseState(state), userId);
    }

    @GetMapping(path = "/owner")
    public List<Booking> getOwnerItemsBookings(@RequestParam(required = false) String state,
                                               @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return bookingService.getOwnerItemsBookings(parseState(state), ownerId);
    }

    private BookingState parseState(String state) {
        BookingState bookingState;
        try {
            bookingState = (state == null) ? BookingState.ALL : BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
        return bookingState;
    }

}
