package ru.practicum.shareit.model.user;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.ValidatedEntity;

/**
 * DTO 'пользователь'
 */
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto implements ValidatedEntity {
    Long id;
    String name;
    @Email(message = "Неверный формат для email") String email;
}