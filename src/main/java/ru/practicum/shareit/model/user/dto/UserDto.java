package ru.practicum.shareit.model.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;
import ru.practicum.shareit.model.user.User;

import java.io.Serializable;

/**
 * DTO для {@link User}
 */
@Value
@AllArgsConstructor
public class UserDto implements Serializable {
    Long id;
    String name;
    @NotNull(message = "Отсутствует email")
    @Email(message = "Некорректный email")
    String email;
}