package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.dto.UserDto;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    static String EMPTY_STRING = "";
    static String USER_ID = "ID ";
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    String thisService = this.getClass().getSimpleName();
    UserRepository users;

    /**
     * Получение 'пользователя' по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return {@link User} со всеми полями
     */
    @Override
    public User get(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString())));
    }

    /**
     * Добавление 'пользователя'
     *
     * @param user {@link User} с необходимыми установленными полями
     * @return {@link User} со всеми полями и установленным ID
     */
    @Override
    public User add(User user) {
        try {
            return users.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(
                    thisService,
                    EMPTY_STRING,
                    e.getMostSpecificCause().getLocalizedMessage());
        }
    }

    /**
     * Обновление существующего 'пользователя'
     *
     * @param user шаблон {@link UserDto} с необходимыми установленными полями
     * @param userId идентификатор целевого 'пользователя'
     * @return обновленный {@link User}
     */
    @Override
    public User update(User user, Long userId) {
        var targetUser = users.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString()))
        );
        Optional.ofNullable(user.getName()).ifPresent(targetUser::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(targetUser::setEmail);
        try {
            return users.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(
                    thisService,
                    EMPTY_STRING,
                    e.getMostSpecificCause().getLocalizedMessage());
        }
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
