package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
public class BookingIntegrationTest {

    private final BookingService bookingService;

    private final BookingDto newBookingDto = BookingDto.builder()
            .start(LocalDateTime.now().plusHours(1))
            .end(LocalDateTime.now().plusHours(2))
            .item(1)
            .build();

    @Test
    public void shouldAddNewBooking() {
        Booking booking = bookingService.createBooking(newBookingDto, 2);
        assertThat(booking.getId(), equalTo(3L));
        assertThat(booking.getItem().getId(), equalTo(1L));
        assertThat(booking.getBooker().getId(), equalTo(2L));
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    public void shouldSetApprovedStatusFoBooking2() {
        Booking booking = bookingService.updateStatus(1, 2, true);
        assertThat(booking.getId(), equalTo(2L));
        assertThat(booking.getItem().getId(), equalTo(1L));
        assertThat(booking.getBooker().getId(), equalTo(2L));
        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void shouldReturnFirstBooking() {
        Booking booking = bookingService.findBooking(1, 1);
        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getItem().getId(), equalTo(1L));
        assertThat(booking.getBooker().getId(), equalTo(2L));
        assertThat(booking.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void shouldReturnTwoBookingsForUser2AndAllStatus() {
        List<Booking> bookings = bookingService.getUserBookings(BookingState.ALL, 2);
        assertThat(bookings.size(), equalTo(2));
        assertThat(bookings.get(0).getId(), equalTo(2L));
        assertThat(bookings.get(0).getItem().getId(), equalTo(1L));
        assertThat(bookings.get(0).getBooker().getId(), equalTo(2L));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.WAITING));
    }

    @Test
    public void shouldReturnOnwBookingsForOwner1AndStatusWaiting() {
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.WAITING, 1);
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getId(), equalTo(2L));
        assertThat(bookings.get(0).getItem().getId(), equalTo(1L));
        assertThat(bookings.get(0).getBooker().getId(), equalTo(2L));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.WAITING));
    }

    @Test
    public void shouldReturnOneBookingWhenStatePastAndPaginated() {
        List<Booking> bookings = bookingService.getUserBookings(BookingState.PAST, 2, 0, 10);
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getId(), equalTo(1L));
        assertThat(bookings.get(0).getItem().getId(), equalTo(1L));
        assertThat(bookings.get(0).getBooker().getId(), equalTo(2L));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.APPROVED));
        LocalDateTime now = LocalDateTime.now();
        assertTrue(bookings.get(0).getStart().isBefore(now));
        assertTrue(bookings.get(0).getEnd().isBefore(now));
    }

    @Test
    public void shouldReturnOneBookingForOwner1WhenFutureAndPaginated() {
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.FUTURE, 1, 0, 10);
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getId(), equalTo(2L));
        assertThat(bookings.get(0).getItem().getId(), equalTo(1L));
        assertThat(bookings.get(0).getBooker().getId(), equalTo(2L));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.WAITING));
        LocalDateTime now = LocalDateTime.now();
        assertTrue(bookings.get(0).getStart().isAfter(now));
        assertTrue(bookings.get(0).getEnd().isAfter(now));
    }


}