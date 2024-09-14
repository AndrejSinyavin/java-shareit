package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    static String USER_NAME_UNKNOWN = "";
    static String USER_ID = "ID ";
    static Long USER_ID_EMPTY = 0L;
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    String thisService = this.getClass().getSimpleName();
    UserRepository users;
    UserMapper mapper;
    CustomEntityValidator validator;

    /**
     * Получение 'пользователя' по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return {@link UserDto} со всеми полями
     */
    @Override
    public UserDto get(Long userId) {
        var user = users.findById(userId)
                .orElseThrow(() ->
                new EntityNotFoundException(thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString())));
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
        userDto.setId(USER_ID_EMPTY);
        var data = (User) validator.validate(mapper.toUser(userDto));
        String name = data.getName();
        if (name == null || name.isBlank()) {
            data.setName(USER_NAME_UNKNOWN);
        }
        try {
            return mapper.toUserDto(users.save(data));
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(thisService, "", e.getMostSpecificCause().getLocalizedMessage());
        }
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
        var user = users.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString()))
        );
        var data = mapper.toUser((UserDto) validator.validate(userDto));
        String name = data.getName();
        if (name != null && name.isBlank()) {
            data.setName(USER_NAME_UNKNOWN);
        }
        Optional.ofNullable(data.getName()).ifPresent(user::setName);
        Optional.ofNullable(data.getEmail()).ifPresent(user::setEmail);
        return mapper.toUserDto(users.save(user));
    }

    /**
     * Удаление 'пользователя'
     *
     * @param userId идентификатор 'пользователя'
     */
    @Override
    public void delete(Long userId) {
        users.deleteById(userId);
    }

}
