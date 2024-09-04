package ru.practicum.shareit.model.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.user.entity.User;
import ru.practicum.shareit.model.user.dto.UserDto;

/**
 * Маппер для преобразования пользователь -> DTO и обратно
 */
@Component
public class UserMapper implements UserMapperBase {

    @Override
    public User toUser(UserDto userDto) {
        return new User(userDto.id(), userDto.name(), userDto.email());
    }

    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
