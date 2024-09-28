package ru.practicum.shareit.model.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    Collection<Booking> getAllByItemIdIn(List<Long> itemIds);

    Optional<Booking> findByItemIsAndBookerIs(Item item, User booker);

}