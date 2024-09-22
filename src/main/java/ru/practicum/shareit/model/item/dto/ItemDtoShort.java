package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * 'Короткий' DTO для {@link ru.practicum.shareit.model.item.Item}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoShort(
        Long id,
        String name
) implements Serializable, ValidatedEntity {
}