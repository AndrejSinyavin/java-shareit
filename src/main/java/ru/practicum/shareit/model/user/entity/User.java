package ru.practicum.shareit.model.user.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.ValidatedEntity;

/**
 * Сущность 'пользователь'
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements ValidatedEntity {
    @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
    final Long id;

    String name;

    @Email(message = "Неверный формат для email")
    @NotNull(message = "Не указан email")
    String email;
}
