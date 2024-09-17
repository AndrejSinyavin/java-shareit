package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityValidateException;

import java.util.Optional;

/**
 * Маппер для преобразования пользователь -> DTO и обратно в контроллере
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapperImpl implements UserMapper {
    static Long ITEM_ID_EMPTY = 0L;
    static String EMPTY_STRING = "";
    static String ABSENT_EMAIL = "Отсутствует обязательный email";
    String thisService = this.getClass().getSimpleName();

    @Override
    public User toAddUser(UserDto userDto) {
        return new User(
                ITEM_ID_EMPTY,
                Optional.ofNullable(userDto.name()).orElse(EMPTY_STRING).trim(),
                Optional.ofNullable(userDto.email()).orElseThrow(() ->
                        new EntityValidateException(thisService, ABSENT_EMAIL))
        );
    }

    @Override
    public User toUpdateUser(UserDto userDto) {
        return new User(
                ITEM_ID_EMPTY,
                userDto.name(),
                userDto.email()
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
