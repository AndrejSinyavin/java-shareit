package ru.practicum.shareit.model.user.mapper;

import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.entity.User;

/**
 * Интерфейс маппера User <-> DTO User
 */
public interface UserMapperBase {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
