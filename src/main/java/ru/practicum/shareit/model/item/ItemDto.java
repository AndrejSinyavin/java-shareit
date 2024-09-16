package ru.practicum.shareit.model.item;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * DTO 'вещь'
 */
@Value
@AllArgsConstructor
public class ItemDto {
        Long id;
        String name;
        String description;
        Boolean available;
}
