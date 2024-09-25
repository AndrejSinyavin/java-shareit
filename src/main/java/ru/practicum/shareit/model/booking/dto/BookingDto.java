package ru.practicum.shareit.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.user.dto.UserDtoShort;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO 'бронирование' {@link Booking}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDto(
        Long id,
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status,
        ItemDtoShort item,
        UserDtoShort booker
) implements Serializable {
}
