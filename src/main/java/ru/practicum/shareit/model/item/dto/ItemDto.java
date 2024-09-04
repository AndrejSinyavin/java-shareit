package ru.practicum.shareit.model.item.dto;

/**
 * DTO вещь
 */
public record ItemDto(
        Long id,
        String name,
        String description,
        Boolean available) {
}
