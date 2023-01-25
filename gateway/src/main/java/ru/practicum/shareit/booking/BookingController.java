package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping(params = {"from", "size"})
    public ResponseEntity<Object> getUserBookingsWithPagination(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                                @PositiveOrZero @RequestParam(name = "from") Integer from,
                                                                @Positive @RequestParam(name = "size") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getUserBookingsWithPagination(userId, state, from, size);
    }

    @GetMapping(path = "/owner")
    public ResponseEntity<Object> getOwnerItemsBookings(@RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                        @RequestHeader("X-Sharer-User-Id") long ownerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getOwnerItemsBookings(ownerId, state);
    }

    @GetMapping(path = "/owner", params = {"from", "size"})
    public ResponseEntity<Object> getOwnerItemsBookingsWithPagination(@PositiveOrZero @RequestParam int from,
                                                                      @Positive @RequestParam int size,
                                                                      @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                                      @RequestHeader("X-Sharer-User-Id") long ownerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getOwnerItemsBookingsWithPagination(ownerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping(path = "/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                               @PathVariable long bookingId,
                                               @RequestParam boolean approved) {
        return bookingClient.updateStatus(ownerId, bookingId, approved);
    }
}
