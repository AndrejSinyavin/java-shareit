package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.validation.NullOrNotBlank;

import java.io.Serializable;

/**
 * DTO 'вещь' для PATCH-обновления DAO 'вещь'. Все поля могут быть null для обновления только
 * поступивших в API запросе полей
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoUpdate(
        @NullOrNotBlank(message = "Пустое название вещи")
        String name,

        @NullOrNotBlank(message = "Пустое описание вещи")
        String description,

        Boolean available

) implements Serializable {
}