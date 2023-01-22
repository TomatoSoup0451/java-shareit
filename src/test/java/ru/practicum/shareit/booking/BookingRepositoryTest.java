package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/reset.sql", "/schema.sql", "/import_test_data.sql"})
class BookingRepositoryTest {

    private final BookingRepository bookingRepository;

    @Test
    public void shouldReturn1BookingWhenSearchByOwnerAndStatus() {
        List<Booking> bookings = bookingRepository.findByOwnerIdAndStatus(1, Status.APPROVED);
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void shouldReturn1BookingWhenSearchByOwnerAndStatusWithPagination() {
        List<Booking> bookings = bookingRepository.findByOwnerIdAndStatus(1, Status.APPROVED,
                PageRequest.of(0, 1));
        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    public void shouldReturn2BookingWhenSearchByOwner() {
        List<Booking> bookings = bookingRepository.findByOwnerId(1L);
        assertThat(bookings.size(), equalTo(2));
    }

    @Test
    public void shouldReturn1BookingWhenSearchByOwnerWithPagination() {
        List<Booking> bookings = bookingRepository.findByOwnerId(1L, PageRequest.of(0, 1));
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturn1BookingWhenSearchByOwnerAndStartsInTheFuture() {
        List<Booking> bookings = bookingRepository.findByOwnerIdAndStartAfter(1L, LocalDateTime.now());
        assertThat(bookings.size(), equalTo(1));
        assertThat(true, is(bookings.get(0).getStart().isAfter(LocalDateTime.now())));
    }

    @Test
    public void shouldReturn1BookingWhenSearchByOwnerAndEndsInThePast() {
        List<Booking> bookings = bookingRepository.findByOwnerIdAndEndBefore(1L, LocalDateTime.now());
        assertThat(bookings.size(), equalTo(1));
        assertThat(true, is(bookings.get(0).getEnd().isBefore(LocalDateTime.now())));
    }
}