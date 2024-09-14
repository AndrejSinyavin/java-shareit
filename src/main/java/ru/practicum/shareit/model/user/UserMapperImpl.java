package ru.practicum.shareit.model.user;

import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования пользователь -> DTO и обратно
 */
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}
