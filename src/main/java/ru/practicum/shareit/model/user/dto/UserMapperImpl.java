package ru.practicum.shareit.model.user.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.user.User;

import java.util.Optional;

/**
 * Маппер для преобразования пользователь -> DTO и обратно
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapperImpl implements UserMapper {
    static Long ITEM_ID_EMPTY = 0L;
    static String EMPTY_STRING = "";

    @Override
    public User toAddUser(UserDto userDto) {
        return new User(
                ITEM_ID_EMPTY,
                Optional.ofNullable(userDto.getName()).orElse(EMPTY_STRING).trim(),
                userDto.getEmail()
        );
    }

    @Override
    public User toUpdateUser(UserDto userDto) {
        return new User(
                ITEM_ID_EMPTY,
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
