package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;

import java.util.Optional;

/**
 * Маппер для преобразования 'пользователь' <-> DTO
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapperImpl implements UserMapper {
    static Long ITEM_ID_EMPTY = 0L;
    static String UNKNOWN_USER = "Неизвестный пользователь";

    @Override
    public User toAddUser(UserDtoCreate user) {
        return new User(
                ITEM_ID_EMPTY,
                Optional.ofNullable(user.name()).orElse(UNKNOWN_USER),
                user.email()
        );
    }

    @Override
    public User toUpdateUser(UserDtoUpdate user) {
        return new User(
                ITEM_ID_EMPTY,
                user.name(),
                user.email()
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
