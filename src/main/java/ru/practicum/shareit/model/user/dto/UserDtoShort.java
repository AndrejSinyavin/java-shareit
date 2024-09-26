package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.user.User;

import java.io.Serializable;

/**
 * 'Короткий' DTO {@link User} для использования в составном ответе фронту
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoShort(
        Long id,
        String name
) implements Serializable {
}