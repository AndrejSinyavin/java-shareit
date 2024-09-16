package ru.practicum.shareit.model.user.dto;

import ru.practicum.shareit.model.user.User;

/**
 * Интерфейс маппера User <-> DTO User
 */
public interface UserMapper {
    User toAddUser(UserDto userDto);

    User toUpdateUser(UserDto userDto);

    UserDto toUserDto(User user);
}
