package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.item.Item;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDto(
        Long id,
        String name,
        String description,
        Boolean available
) implements Serializable {
}
