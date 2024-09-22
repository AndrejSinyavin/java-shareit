package ru.practicum.shareit.model.user;

import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;

/**
 * Маппинг 'User <-> DTO'
 */
public interface UserMapper {

    User toAddUser(UserDtoCreate userDto);

    User toUpdateUser(UserDtoUpdate userDto);

    UserDto toUserDto(User user);
}
