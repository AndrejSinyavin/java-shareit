package ru.practicum.shareit.model.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validate.Validated;

/**
 * DTO 'вещь'
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto implements Validated {
        Long id;
        String name;
        String description;
        Boolean available;
}
