package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoUpdate(
        String name,
        String description,
        Boolean available
) implements Serializable, ValidatedEntity {
}