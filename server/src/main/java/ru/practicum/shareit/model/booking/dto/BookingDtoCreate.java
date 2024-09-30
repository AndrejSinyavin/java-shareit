package ru.practicum.shareit.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для создания {@link Booking}, используется только для создания необходимых полей в DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDtoCreate(
        @Positive(message = "ID 'предмета' должно быть больше нуля")
        @NotNull(message = "Отсутствует ID 'предмета'")
        Long itemId,

        @NotNull(message = "Отсутствует дата начала 'бронирования'")
        LocalDateTime start,

        @NotNull(message = "Отсутствует дата завершения 'бронирования'")
        LocalDateTime end
) implements Serializable, ValidatedEntity {
}