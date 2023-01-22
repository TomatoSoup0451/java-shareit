package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndStatusOrderByIdDesc(long bookerId, Status status);

    List<Booking> findByBookerIdAndStatusOrderByIdDesc(long bookerId, Status status, Pageable pageable);

    List<Booking> findByBookerIdOrderByIdDesc(long bookerId);

    List<Booking> findByBookerIdOrderByIdDesc(long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStartAfterOrderByIdDesc(long bookerId, LocalDateTime startTime);

    List<Booking> findByBookerIdAndStartAfterOrderByIdDesc(long bookerId, LocalDateTime startTime, Pageable pageable);

    List<Booking> findByBookerIdAndEndBeforeOrderByIdDesc(long bookerId, LocalDateTime endTime);

    List<Booking> findByBookerIdAndEndBeforeOrderByIdDesc(long bookerId, LocalDateTime endTime, Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(long bookerId, LocalDateTime startTime,
                                                                       LocalDateTime endTime);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByIdDesc(long bookerId, LocalDateTime startTime,
                                                                       LocalDateTime endTime, Pageable pageable);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.status = :status order by b.id desc ")
    List<Booking> findByOwnerIdAndStatus(long ownerId, Status status);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.status = :status order by b.id desc ")
    List<Booking> findByOwnerIdAndStatus(long ownerId, Status status, Pageable pageable);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId order by b.id desc ")
    List<Booking> findByOwnerId(long ownerId);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId order by b.id desc ")
    List<Booking> findByOwnerId(long ownerId, Pageable pageable);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.start > :time order by b.id desc ")
    List<Booking> findByOwnerIdAndStartAfter(long ownerId, LocalDateTime time);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.start > :time order by b.id desc ")
    List<Booking> findByOwnerIdAndStartAfter(long ownerId, LocalDateTime time, Pageable pageable);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.end < :time order by b.id desc ")
    List<Booking> findByOwnerIdAndEndBefore(long ownerId, LocalDateTime time);

    @Query(" select b from Booking b where b.item.owner.id = :ownerId and b.end < :time order by b.id desc ")
    List<Booking> findByOwnerIdAndEndBefore(long ownerId, LocalDateTime time, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id = :ownerId and b.start < :time and b.end > :time order by b.id desc ")
    List<Booking> findByOwnerIdAndStartBeforeAndEndAfter(long ownerId, LocalDateTime time);

    @Query(" select b from Booking b " +
            "where b.item.owner.id = :ownerId and b.start < :time and b.end > :time order by b.id desc ")
    List<Booking> findByOwnerIdAndStartBeforeAndEndAfter(long ownerId, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemIdAndStartBeforeOrderByStartDesc(long itemId, LocalDateTime time);

    List<Booking> findByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);
}
