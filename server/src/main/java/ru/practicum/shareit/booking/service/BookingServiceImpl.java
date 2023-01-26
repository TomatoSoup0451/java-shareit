package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.IllegalBookingAccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, long bookerId) {
        Item item = itemRepository.findById(bookingDto.getItem())
                .orElseThrow(() -> new IdNotFoundException("Item with id = " +
                        bookingDto.getItem() + " not found"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item with id = " + item.getId() + " is not available");
        }
        if (item.getOwner().getId() == bookerId) {
            throw new IllegalBookingAccessException("User with id = " + bookerId + " is owner" +
                    " and can't book item with id = " + item.getId());
        }
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto,
                userRepository.findById(bookerId)
                        .orElseThrow(() -> new IdNotFoundException("User with id = " + bookerId + " not found")),
                item, Status.WAITING));
        log.info("Booking with id = {} added", booking.getId());
        return booking;
    }

    @Override
    public Booking updateStatus(long ownerId, long bookingId, boolean approved) {
        Booking oldBooking = getUncheckedBookingById(bookingId);
        if (oldBooking.getItem().getOwner().getId() != ownerId) {
            throw new IdNotFoundException("User with id = " + ownerId + " doesn't have access" +
                    " to booking with id = " + bookingId);
        }
        if (oldBooking.getStatus().name().equals("APPROVED") && approved
                || oldBooking.getStatus().name().equals("REJECTED") && !approved) {
            throw new BadRequestException("Booking status with id = " + bookingId
                    + " is already " + oldBooking.getStatus());
        }
        oldBooking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        Booking updatedBooking = bookingRepository.save(oldBooking);
        log.info("Booking with id = {} updated", updatedBooking.getId());
        return updatedBooking;
    }


    @Override
    public Booking findBooking(long userId, long bookingId) {
        Booking booking = getUncheckedBookingById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new IllegalBookingAccessException("User with id = " + userId + " doesn't have access" +
                    " to booking with id = " + bookingId);
        }
        return booking;
    }

    @Override
    public List<Booking> getUserBookings(BookingState state, long bookerId) {
        if (!userRepository.existsById(bookerId)) {
            throw new IdNotFoundException("User with id = " + bookerId + " not found");
        }
        switch (state) {
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId, Status.WAITING);
            case FUTURE:
                return bookingRepository
                        .findByBookerIdAndStartAfterOrderByIdDesc(bookerId, LocalDateTime.now());
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId, Status.REJECTED);
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(bookerId, now, now);
            case PAST:
                return bookingRepository.findByBookerIdAndEndBeforeOrderByIdDesc(bookerId,
                        LocalDateTime.now());
            case ALL:
                return bookingRepository.findByBookerIdOrderByIdDesc(bookerId);
            default:
                throw new UnsupportedOperationException("Unknown state: " + state.name());
        }
    }

    @Override
    public List<Booking> getOwnerItemsBookings(BookingState state, long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new IdNotFoundException("User with id = " + ownerId + " not found");
        }
        switch (state) {
            case WAITING:
                return bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING);
            case FUTURE:
                return bookingRepository
                        .findByOwnerIdAndStartAfter(ownerId, LocalDateTime.now());
            case REJECTED:
                return bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED);
            case CURRENT:
                return bookingRepository.findByOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now());
            case PAST:
                return bookingRepository.findByOwnerIdAndEndBefore(ownerId, LocalDateTime.now());
            case ALL:
                return bookingRepository.findByOwnerId(ownerId);
            default:
                throw new UnsupportedOperationException("Unknown state: " + state.name());
        }
    }

    @Override
    public List<Booking> getUserBookings(BookingState state, long bookerId, Pageable pageable) {
        if (!userRepository.existsById(bookerId)) {
            throw new IdNotFoundException("User with id = " + bookerId + " not found");
        }
        switch (state) {
            case WAITING:
                return bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId, Status.WAITING, pageable);
            case FUTURE:
                return bookingRepository
                        .findByBookerIdAndStartAfterOrderByIdDesc(bookerId, LocalDateTime.now(), pageable);
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatusOrderByIdDesc(bookerId, Status.REJECTED, pageable);
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(bookerId, now,
                        now, pageable);
            case PAST:
                return bookingRepository.findByBookerIdAndEndBeforeOrderByIdDesc(bookerId,
                        LocalDateTime.now(), pageable);
            case ALL:
                return bookingRepository.findByBookerIdOrderByIdDesc(bookerId, pageable);
            default:
                throw new UnsupportedOperationException("Unknown state: " + state.name());
        }
    }

    @Override
    public List<Booking> getOwnerItemsBookings(BookingState state, long ownerId, Pageable pageable) {
        if (!userRepository.existsById(ownerId)) {
            throw new IdNotFoundException("User with id = " + ownerId + " not found");
        }
        switch (state) {
            case WAITING:
                return bookingRepository.findByOwnerIdAndStatus(ownerId, Status.WAITING, pageable);
            case FUTURE:
                return bookingRepository
                        .findByOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), pageable);
            case REJECTED:
                return bookingRepository.findByOwnerIdAndStatus(ownerId, Status.REJECTED, pageable);
            case CURRENT:
                return bookingRepository.findByOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), pageable);
            case PAST:
                return bookingRepository.findByOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), pageable);
            case ALL:
                return bookingRepository.findByOwnerId(ownerId, pageable);
            default:
                throw new UnsupportedOperationException("Unknown state: " + state.name());
        }
    }

    private Booking getUncheckedBookingById(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IdNotFoundException("Booking with id = " + bookingId + " not found"));
    }
}
