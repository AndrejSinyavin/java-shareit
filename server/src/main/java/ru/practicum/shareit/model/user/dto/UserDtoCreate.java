package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO для создания 'пользователя' {@link ru.practicum.shareit.model.user.User}. Минимальный необходимый набор полей.
 * Обязательный email для однозначной идентификации пользователя, поле 'name' необязательное
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoCreate(
        @NullOrNotBlank(message = "Пустое имя пользователя")
        String name,

        @NotNull(message = "Отсутствует email")
        @Email(message = "Неверный формат для email")
        String email
) implements Serializable, ValidatedEntity {
}