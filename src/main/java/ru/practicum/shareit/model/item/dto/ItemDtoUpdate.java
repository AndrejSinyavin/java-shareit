package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoUpdate(
        @NullOrNotBlank(message = "Пустое название вещи")
        String name,

        @NullOrNotBlank(message = "Пустое описание вещи")
        String description,

        @Nullable
        Boolean available
) implements Serializable, ValidatedEntity {
}