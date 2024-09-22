package ru.practicum.shareit.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для создания {@link Booking}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDtoCreate(
        @Positive(message = "ID 'предмета' должно быть больше нуля")
        @NotNull(message = "Отсутствует ID 'предмета'")
        Long itemId,

        @FutureOrPresent(message = "Дата начала 'бронирования' не может быть в прошлом")
        @NotNull(message = "Отсутствует дата начала 'бронирования'")
        LocalDateTime start,

        @FutureOrPresent(message = "Дата завершения 'бронирования' не может быть в прошлом")
        @NotNull(message = "Отсутствует дата завершения 'бронирования'")
        LocalDateTime end
) implements Serializable, ValidatedEntity {
}