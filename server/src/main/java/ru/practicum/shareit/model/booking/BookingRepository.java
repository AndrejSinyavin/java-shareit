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

    Collection<Booking> findAllByStartAfterOrderByStartDesc(Instant now);

    Collection<Booking> findAllByEndBeforeOrderByStartDesc(Instant now);

    Collection<Booking> findAllByItemIdInOrderByStartDesc(Set<Long> itemIds);

    Collection<Booking> findAllByItemIdInAndStatusIsOrderByStartDesc(
            Set<Long> itemIds, BookingStatus bookingStatus);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartDesc(
            Set<Long> itemIds, Instant now);

    Collection<Booking> findAllByItemIdInAndEndBeforeOrderByStartDesc(
            Set<Long> itemIds, Instant now);

    Collection<Booking> getAllByItemIdIn(List<Long> itemIds);

    Optional<Booking> findByItemAndBooker(Item item, User booker);

}