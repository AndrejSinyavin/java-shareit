package ru.practicum.shareit.model.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.entity.User;
import ru.practicum.shareit.model.user.mapper.UserMapperBase;
import ru.practicum.shareit.model.user.repository.UserRepository;
import ru.practicum.shareit.validation.CustomEntityValidator;

/**
 * Реализация интерфейса {@link UserService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {
    UserRepository users;
    UserMapperBase mapper;
    CustomEntityValidator validator;
    static final String USER_UNKNOWN = "Пользователь";

    /**
     * Получение 'пользователя' по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return {@link UserDto} со всеми полями
     */
    @Override
    public UserDto get(Long userId) {
        var user = users.get(userId);
        return mapper.toUserDto(user);
    }

    /**
     * Добавление 'пользователя'
     *
     * @param userDto {@link UserDto} с необходимыми установленными полями
     * @return {@link UserDto} со всеми полями и установленным ID
     */
    @Override
    public UserDto add(UserDto userDto) {
        var data = (User) validator.validate(mapper.toUser(userDto));
        String name = data.getName();
        if (name == null || name.isBlank()) {
            data.setName(USER_UNKNOWN);
        }
        return mapper.toUserDto(users.add(data));
    }

    /**
     * Обновление существующего 'пользователя'
     *
     * @param userDto {@link UserDto} с необходимыми установленными полями
     * @param userId идентификатор 'пользователя'
     * @return {@link UserDto} со всеми полями
     */
    @Override
    public UserDto update(UserDto userDto, Long userId) {
        var data = mapper.toUser((UserDto) validator.validate(userDto));
        return mapper.toUserDto(users.update(data, userId));
    }

    /**
     * Удаление 'пользователя'
     *
     * @param userId идентификатор 'пользователя'
     */
    @Override
    public void delete(Long userId) {
        users.delete(userId);
    }

}
