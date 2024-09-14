package ru.practicum.shareit.model.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.ValidatedEntity;

/**
 * DTO 'вещь'
 */
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto implements ValidatedEntity {
        Long id;
        String name;
        String description;
        Boolean available;
}
