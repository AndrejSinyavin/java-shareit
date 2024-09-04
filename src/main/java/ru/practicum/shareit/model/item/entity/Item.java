package ru.practicum.shareit.model.item.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.request.ItemRequest;
import ru.practicum.shareit.model.user.entity.User;

/**
 * Сущность вещь
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
    final Long id;

    @NotBlank(message = "Отсутствует название вещи")
    String name;

    @NotBlank(message = "Отсутствует описание вещи")
    String description;

    @NotNull(message = "Не определена доступность вещи")
    Boolean available;

    @NotNull(message = "Отсутствует владелец вещи")
    User owner;

    ItemRequest request;
}