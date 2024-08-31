package ru.practicum.shareit.model.user.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;

/**
 * DTO пользователь
 */

public record UserDto(
        @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
        Long id,
        String name,
        @Email(message = "Неверный формат для email")
        String email) {
}
