package ru.practicum.shareit.model.user;

/**
 * Интерфейс маппинг 'User <-> DTO'
 */
public interface UserMapper {

    User toAddUser(UserDto userDto);

    User toUpdateUser(UserDto userDto);

    UserDto toUserDto(User user);
}
