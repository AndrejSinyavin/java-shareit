package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item}, используется только для создания нового DAO 'вещь'. Null поля не допустимы согласно ТЗ
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoCreate(
        @NotBlank(message = "Не указано название вещи")
        String name,

        @NotBlank(message = "Не указано описание вещи")
        String description,

        @NotNull(message = "Не указана доступность вещи")
        Boolean available
) implements Serializable, ValidatedEntity {
}