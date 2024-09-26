package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO для PATCH-обновления 'пользователя' {@link ru.practicum.shareit.model.user.User}. Все поля могут быть null
 * для выборочного обновления полей в DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoUpdate(
        @NullOrNotBlank(message = "Пустое имя пользователя")
        String name,

        @Email(message = "Неверный формат для email")
        String email
) implements Serializable, ValidatedEntity {
}