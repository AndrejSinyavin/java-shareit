package ru.practicum.shareit.model.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validate.Validated;

/**
 * DTO 'пользователь'
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto implements Validated {
        Long id;
        String name;

        @Email(message = "Неверный формат для email")
        String email;
}
