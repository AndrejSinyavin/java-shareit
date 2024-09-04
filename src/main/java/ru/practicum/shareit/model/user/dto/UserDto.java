package ru.practicum.shareit.model.user.dto;

import jakarta.validation.constraints.Email;

/**
 * DTO пользователь
 */

public record UserDto(
        Long id,
        String name,

        @Email(message = "Неверный формат для email")
        String email) {
}
