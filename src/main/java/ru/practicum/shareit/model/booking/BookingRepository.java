package ru.practicum.shareit.model.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdIsOrderByStartDesc(Long userId);

    Collection<Booking> findAllByBookerIdIsAndStatusIsOrderByStartDesc(
            Long userId, BookingStatus bookingStatus);

    Collection<Booking> findAllByBookerIdIsAndStartAfterOrderByStartDesc(Long userId, Instant now);

    Collection<Booking> findAllByBookerIdIsAndEndBeforeOrderByStartDesc(Long userId, Instant now);

    Collection<Booking> findAllByBookerIdIsAndItemIdInOrderByStartDesc(Long ownerId, Set<Long> itemIds);

    Collection<Booking> findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
            Long ownerId, Set<Long> itemIds, BookingStatus bookingStatus);

    Collection<Booking> findAllByBookerIdIsAndItemIdInAndStartAfterOrderByStartDesc(
            Long ownerId, Set<Long> itemIds, Instant now);

    Collection<Booking> findAllByBookerIdIsAndItemIdInAndEndBeforeOrderByStartDesc(
            Long ownerId, Set<Long> itemIds, Instant now);
}