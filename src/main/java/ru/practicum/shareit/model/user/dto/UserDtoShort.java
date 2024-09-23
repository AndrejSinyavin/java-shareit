package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * 'Короткий' DTO для возврата {@link User} на фронт-энд
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoShort(
        Long id,
        String name
) implements Serializable, ValidatedEntity {
}