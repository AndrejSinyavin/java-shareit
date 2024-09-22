package ru.practicum.shareit.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.user.dto.UserDtoShort;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO 'бронирование' {@link Booking}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDto(
        Long id,
        @FutureOrPresent(message = "Дата начала 'бронирования' не может быть в прошлом")
        @NotNull(message = "Отсутствует дата начала 'бронирования'")
        LocalDateTime start,

        @FutureOrPresent(message = "Дата завершения 'бронирования' не может быть в прошлом")
        @NotNull(message = "Отсутствует дата завершения 'бронирования'")
        LocalDateTime end,
        BookingStatus status,
        ItemDtoShort item,
        UserDtoShort booker
) implements Serializable, ValidatedEntity {
}
