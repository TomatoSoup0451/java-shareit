package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndStatusOrderByIdDesc(long bookerId, Status status);

    List<Booking> findByBookerIdOrderByIdDesc(long bookerId);

    List<Booking> findByBookerIdAndStartAfterOrderByIdDesc(long bookerId, LocalDateTime startTime);

    List<Booking> findByBookerIdAndEndBeforeOrderByIdDesc(long bookerId, LocalDateTime endTime);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(long bookerId, LocalDateTime startTime,
                                                                       LocalDateTime endTime);

    @Query(" select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.id desc ")
    List<Booking> findByOwnerIdAndStatus(long ownerId, Status status);

    @Query(" select b from Booking b where b.item.owner.id = ?1 order by b.id desc ")
    List<Booking> findByOwnerId(long ownerId);

    @Query(" select b from Booking b where b.item.owner.id = ?1 and b.start > ?2 order by b.id desc ")
    List<Booking> findByOwnerIdAndStartAfter(long ownerId, LocalDateTime time);

    @Query(" select b from Booking b where b.item.owner.id = ?1 and b.end < ?2 order by b.id desc ")
    List<Booking> findByOwnerIdAndEndBefore(long ownerId, LocalDateTime time);

    @Query(" select b from Booking b where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2 order by b.id desc ")
    List<Booking> findByOwnerIdAndStartBeforeAndEndAfter(long ownerId, LocalDateTime time);

    List<Booking> findByItemIdAndStartBeforeOrderByStartDesc(long itemId, LocalDateTime time);

    List<Booking> findByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);
}
