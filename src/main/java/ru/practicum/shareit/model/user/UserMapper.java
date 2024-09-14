package ru.practicum.shareit.model.user;

/**
 * Интерфейс маппера User <-> DTO User
 */
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
