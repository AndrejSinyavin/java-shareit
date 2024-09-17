package ru.practicum.shareit.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO 'пользователь' {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(
        Long id,
        String name,

        @Email(message = "Неверный формат для email")
        String email
) implements Serializable, ValidatedEntity {
}