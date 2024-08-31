package ru.practicum.shareit.model.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.user.entity.User;
import ru.practicum.shareit.model.user.dto.UserDto;

@Component
public class UserMapper {

    public User toUser(UserDto userDto) {
        return new User(userDto.id(), userDto.name(), userDto.email());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
