package ru.practicum.shareit.model.booking;

import java.util.Collection;

/**
 * Интерфейс сервисов для работы с 'вещами'
 */
public interface BookingService {

    Booking add(Booking booking, Long itemId, Long bookerId);

    Booking approve(Long bookingId, Long ownerId, Boolean approved);

    Booking getBooking(Long bookingId, Long userId);

    Collection<Booking> getBookingsByUser(Long userId, BookingSearchCriteria state);

    Collection<Booking> getBookingsByAllUserItems(Long ownerId, BookingSearchCriteria state);

}
