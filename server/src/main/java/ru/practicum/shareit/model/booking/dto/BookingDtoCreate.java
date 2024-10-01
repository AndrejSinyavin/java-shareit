package ru.practicum.shareit.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.booking.Booking;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для создания {@link Booking}, используется только для создания необходимых полей в DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDtoCreate(
        Long itemId,
        LocalDateTime start,
        LocalDateTime end
) implements Serializable {
}