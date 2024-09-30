package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для создания 'запроса на аренду вещи', используется только для создания необходимых полей в DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDtoCreate(
        @Positive(message = "ID 'предмета' должно быть больше нуля")
        @NotNull(message = "Отсутствует ID 'предмета'")
        Long itemId,

        @NotNull(message = "Отсутствует дата начала 'бронирования'")
        @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
        LocalDateTime start,

        @NotNull(message = "Отсутствует дата завершения 'бронирования'")
        @FutureOrPresent(message = "Дата окончания бронирования не может быть в прошлом")
        LocalDateTime end
) implements Serializable, ValidatedEntity {
}