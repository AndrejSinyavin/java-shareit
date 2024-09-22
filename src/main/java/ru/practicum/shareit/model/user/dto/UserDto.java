package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO 'пользователь' {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(
        @NotNull(message = "Отсутствует ID")
        @Positive(message = "ID должно быть положительным значением")
        Long id,

        @NotBlank(message = "Пустое имя пользователя")
        String name,

        @NotNull(message = "Отсутствует email")
        @Email(message = "Неверный формат для email")
        String email
) implements Serializable, ValidatedEntity {
}