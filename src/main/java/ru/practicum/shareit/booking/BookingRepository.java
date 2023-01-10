package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enumBooking.Status;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    default Booking get(Long id){
        return findById(id).orElseThrow(() -> new NotFoundException("Booking with id=" + id + " not found"));
    }

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b where  b.booker.id=?1 and b.start>?2 order by b.start desc ")
    List<Booking> findAllFutureForBooker(Long userId, LocalDateTime dateTime);

    @Query("select b from Booking b where b.item.owner.id=?1 and b.start>?2 order by b.start desc")
    List<Booking> findAllFutureForOwner(Long userId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusEqualsOrderByStartDesc(Long userid, Status status);

    @Query("select b from Booking b where b.booker.id=?1 and b.start<=?2 and b.end>=?2 order by b.start")
    List<Booking> findAllByBookerCurrent(Long bookerId, LocalDateTime now);

    @Query("select b from Booking b where b.item.owner.id=?1 order by b.start desc")
    List<Booking> findAllByOwnerOrderByStartDesc(Long userId);

    @Query("select b from Booking b where b.item.owner.id=?1 and b.start<=?2 and b.end>=?2 order by b.start")
    List<Booking> findAllByOwnerCurrent(Long ownerId, LocalDateTime now);

    @Query("select b from Booking b where b.item.owner.id =?1 and b.end< ?2 order by b.start desc")
    List<Booking> findAllByOwnerAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id=?1 and b.status=?2 order by b.start desc ")
    List<Booking> findAllByOwnerAndStatusOrderByStartDesc(Long userId, Status status);

    @Query("select b from Booking b where b.item.id = ?1 and b.end<?2 order by b.start desc")
    Booking findLastBookingByItemId(Long itemId, LocalDateTime now);

    @Query("select b from Booking b where b.item.id=?1 and b.start>?2 order by b.start desc")
    Booking findNextBookingByItemId(Long itemId, LocalDateTime now);

    @Query("select count (b) from Booking b where b.booker.id=?1 and b.item.id=?2 and b.end<?3 " +
            "and b.status= ru.practicum.shareit.booking.enumBooking.Status.APPROVED")
    Integer countCompletedBooking(Long bookerId, Long itemId, LocalDateTime now);
}