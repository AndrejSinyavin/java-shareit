package ru.practicum.shareit.model.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO 'запрос вещи на бронирование' {@link ru.practicum.shareit.model.request.ItemRequest}.
 * Используется для возврата на фронт созданного запроса на бронирование вещи, отсутствующей в общем списке вещей
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRequestDto(
        Long id,
        String description,
        LocalDateTime created
) implements Serializable {
}