package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.user.User;

import java.io.Serializable;

/**
 * 'Короткий' DTO {@link User}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoShort(
        Long id,
        String name
) implements Serializable {
}