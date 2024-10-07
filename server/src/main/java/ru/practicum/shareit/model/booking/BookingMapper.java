package ru.practicum.shareit.model.booking;

import ru.practicum.shareit.model.booking.dto.BookingDto;
import ru.practicum.shareit.model.booking.dto.BookingDtoCreate;

/**
 * Интерфейс маппинг 'Booking <-> DTO'
 */
public interface BookingMapper {

    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDtoCreate bookingDto);

}