package ru.practicum.shareit.model.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO для создания 'пользователя' {@link ru.practicum.shareit.model.user.User}. Минимальный необходимый набор полей.
 * Обязательный email для однозначной идентификации пользователя, поле 'name' необязательное
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDtoCreate(
        String name,
        String email
) implements Serializable {
}