package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.IllegalBookingAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceUnitTest {

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    ItemRepository mockItemRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    public void shouldThrowIllegalBookingAccessExceptionWhenCreateByWrongUser() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(Item.builder()
                        .id(1)
                        .available(true)
                        .owner(User.builder().id(1).build())
                        .build()));
        final IllegalBookingAccessException e = assertThrows(IllegalBookingAccessException.class,
                () -> bookingService.createBooking(BookingDto.builder().build(), 1));
        assertThat(e.getMessage(),
                equalTo("User with id = 1 is owner and can't book item with id = 1"));
    }

    @Test
    public void shouldThrowIdNotFoundExceptionWhenCreateAndItemNotFound() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        final IdNotFoundException e = assertThrows(IdNotFoundException.class,
                () -> bookingService.createBooking(BookingDto.builder().item(1).build(), 1));
        assertThat(e.getMessage(),
                equalTo("Item with id = 1 not found"));
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenItemNotAvailable() {
        Mockito
                .when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(Item.builder().id(1).available(false).build()));
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(BookingDto.builder().item(1).build(), 1));
        assertThat(e.getMessage(),
                equalTo("Item with id = 1 is not available"));
    }

    @Test
    public void shouldThrowIdNotFoundWhenUpdatedByWrongUsers() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(Booking.builder()
                        .item(Item.builder()
                                .owner(User.builder().id(1).build())
                                .build())
                        .build()));
        final IdNotFoundException e = assertThrows(IdNotFoundException.class,
                () -> bookingService.updateStatus(99, 1, true));
        assertThat(e.getMessage(),
                equalTo("User with id = 99 doesn't have access to booking with id = 1"));
    }

    @Test
    public void shouldThrowBadRequestExceptionWhenUpdateWithSameStatus() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(Booking.builder()
                        .item(Item.builder()
                                .owner(User.builder().id(1).build())
                                .build())
                        .status(Status.APPROVED)
                        .build()));
        final BadRequestException e = assertThrows(BadRequestException.class,
                () -> bookingService.updateStatus(1, 1, true));
        assertThat(e.getMessage(),
                equalTo("Booking status with id = 1 is already APPROVED"));
    }

    @Test
    public void shouldThrowIllegalBookingAccessExceptionWhenGetByWrongUser() {
        Mockito
                .when(mockBookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(Booking.builder()
                        .id(1)
                        .item(Item.builder()
                                .owner(User.builder().id(1).build())
                                .build())
                        .booker(User.builder().id(2).build())
                        .status(Status.APPROVED)
                        .build()));
        final IllegalBookingAccessException e = assertThrows(IllegalBookingAccessException.class,
                () -> bookingService.findBooking(99, 1));
        assertThat(e.getMessage(),
                equalTo("User with id = 99 doesn't have access to booking with id = 1"));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusWaiting() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatusOrderByIdDesc(1, Status.WAITING))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.WAITING, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusFuture() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndStartAfterOrderByIdDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.FUTURE, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusRejected() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatusOrderByIdDesc(1, Status.REJECTED))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.REJECTED, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusCurrent() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(anyLong(),
                                any(LocalDateTime.class),
                                any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.CURRENT, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusPast() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndEndBeforeOrderByIdDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.PAST, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusAll() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdOrderByIdDesc(1))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.ALL, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusWaitingWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatusOrderByIdDesc(1, Status.WAITING,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.WAITING, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusFutureWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndStartAfterOrderByIdDesc(anyLong(), any(LocalDateTime.class),
                                any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.FUTURE, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusRejectedWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdAndStatusOrderByIdDesc(1, Status.REJECTED,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.REJECTED, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusCurrentWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(anyLong(),
                                any(LocalDateTime.class),
                                any(LocalDateTime.class),
                                any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.CURRENT, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusPastWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByBookerIdAndEndBeforeOrderByIdDesc(anyLong(), any(LocalDateTime.class),
                                any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.PAST, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetUserBookingsAndStatusAllWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByBookerIdOrderByIdDesc(1,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getUserBookings(BookingState.ALL, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusWaiting() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(1, Status.WAITING))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.WAITING, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusFuture() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.FUTURE, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOnwerBookingsAndStatusRejected() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(1, Status.REJECTED))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.REJECTED, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusCurrent() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.CURRENT, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusPast() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.PAST, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusAll() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerId(1))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.ALL, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusWaitingWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(1, Status.WAITING,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.WAITING, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusFutureWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndStartAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.FUTURE, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOnwerBookingsAndStatusRejectedWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerIdAndStatus(1, Status.REJECTED,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.REJECTED, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusCurrentWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(LocalDateTime.class),
                                any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.CURRENT, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusPastWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository
                        .findByOwnerIdAndEndBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.PAST, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }

    @Test
    public void shouldReturnListWith1ElementWhenGetOwnerBookingsAndStatusAllWithPageable() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(User.builder().build()));
        Mockito
                .when(mockBookingRepository.findByOwnerId(1,
                        PageRequest.of(0, 1, Sort.by("id").ascending())))
                .thenReturn(List.of(Booking.builder().build()));
        List<Booking> bookings = bookingService.getOwnerItemsBookings(BookingState.ALL, 1, 0, 1);
        assertThat(bookings.size(), equalTo(1));
    }
}
