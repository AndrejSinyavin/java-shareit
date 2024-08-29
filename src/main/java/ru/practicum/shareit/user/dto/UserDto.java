package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO пользователь
 */
@Data
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    String email;

}
