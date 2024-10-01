package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO для PATCH-обновления 'пользователя' {@link ru.practicum.shareit.model.user.User}. Все поля могут быть null
 * для выборочного обновления полей в DAO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoUpdate(
        String name,
        String email
) implements Serializable {
}