package ru.practicum.shareit.model.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.entity.User;

/**
 * Маппер для преобразования пользователь -> DTO и обратно
 */
@Component
public class UserMapper implements UserMapperBase {

    @Override
    public User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }

    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

}
