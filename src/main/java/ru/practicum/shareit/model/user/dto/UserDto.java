package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.user.User;

import java.io.Serializable;

/**
 * DTO 'пользователь' {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDto(
        Long id,
        String name,
        String email
) implements Serializable {
}