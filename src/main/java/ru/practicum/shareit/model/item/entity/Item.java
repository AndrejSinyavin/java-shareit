package ru.practicum.shareit.model.item.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.request.ItemRequest;
import ru.practicum.shareit.validate.Validated;

/**
 * Сущность 'вещь'
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item implements Validated {
    @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
    final Long id;

    @NotBlank(message = "Не указано название вещи")
    String name;

    @NotBlank(message = "Не указано описание вещи")
    String description;

    @NotNull(message = "Не указана доступность вещи")
    Boolean available;

    @NotNull(message = "Не указан владелец вещи")
    Long owner;

    ItemRequest request;
}