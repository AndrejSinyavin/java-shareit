package ru.practicum.shareit.model.booking;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.booking.dto.BookingDto;
import ru.practicum.shareit.model.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.user.dto.UserDtoShort;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Реализация интерфейса {@link BookingMapper}
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingMapperImpl implements BookingMapper {
    static Long BOOKING_ID_EMPTY = 0L;

    @Override
    public BookingDto toBookingDto(Booking booking) {
        var item = booking.getItem();
        var booker = booking.getBooker();
        return new BookingDto(
                booking.getId(),
                LocalDateTime.ofInstant(booking.getStart(), ZoneOffset.UTC),
                LocalDateTime.ofInstant(booking.getEnd(), ZoneOffset.UTC),
                booking.getStatus(),
                new ItemDtoShort(item.getId(), item.getName()),
                new UserDtoShort(booker.getId(), booker.getName())
        );
    }

    @Override
    public Booking toBooking(BookingDtoCreate bookingDto) {
        return new Booking(
                BOOKING_ID_EMPTY,
                bookingDto.start().toInstant(ZoneOffset.UTC),
                bookingDto.end().toInstant(ZoneOffset.UTC),
                null,
                null,
                BookingStatus.WAITING
        );
    }
}
