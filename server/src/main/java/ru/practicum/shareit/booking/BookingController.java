package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") long bookerId) {
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
        return bookingService.getUserBookings(BookingState.valueOf(state), userId);
    }

    @GetMapping(params = {"from", "size"})
    public List<Booking> getUserBookingsWithPagination(@RequestParam int from,
                                                       @RequestParam int size,
                                                       @RequestParam String state,
                                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getUserBookings(BookingState.valueOf(state), userId, getPageable(from, size));
    }

    @GetMapping(path = "/owner")
    public List<Booking> getOwnerItemsBookings(@RequestParam(required = false) String state,
                                               @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return bookingService.getOwnerItemsBookings(BookingState.valueOf(state), ownerId);
    }

    @GetMapping(path = "/owner", params = {"from", "size"})
    public List<Booking> getOwnerItemsBookingsWithPagination(@RequestParam int from,
                                                             @RequestParam int size,
                                                             @RequestParam String state,
                                                             @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return bookingService.getOwnerItemsBookings(BookingState.valueOf(state), ownerId, getPageable(from, size));
    }


    private Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }
}
