package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.item.Item;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item} для PATCH обновления DAO 'вещь'. Все поля могут быть null для обновления только
 * поступивших в API запросе полей
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoUpdate(
        String name,
        String description,
        Boolean available
) implements Serializable {
}