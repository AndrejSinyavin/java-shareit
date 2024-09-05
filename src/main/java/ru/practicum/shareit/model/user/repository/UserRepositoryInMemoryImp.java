package ru.practicum.shareit.model.user.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация интерфейса {@link UserRepository} для хранения пользователей в памяти
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class UserRepositoryInMemoryImp implements UserRepository {
    static final String USER_NOT_FOUND = "Не найден 'пользователь' ID: ";
    static final String EMAIL_EXIST = "'Пользователь' с таким email уже существует ";
    static final String USER_ALREADY_EXIST = "'Пользователь' с указанным ID уже существует: ";
    static final String ENTITY_CREATE_ERROR = "Ошибка при создании сущности ";
    static final String ENTITY_UPDATE_ERROR = "Ошибка при обновлении сущности ";
    static final String EMPTY_STRING = "";
    final String thisService = this.getClass().getSimpleName();
    final Map<Long, User> users = new HashMap<>();
    Long idCount = 0L;

    /**
     * Получение 'пользователя'
     *
     * @param userId идентификатор 'пользователя'
     * @return 'пользователь' {@link User}
     */
    @Override
    public User get(Long userId) {
        var user = users.get(userId);
        if (user == null) {
            log.warn(USER_NOT_FOUND.concat(userId.toString()));
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, EMPTY_STRING);
        } else {
            return new User(userId, user.getName(), user.getEmail());
        }
    }

    /**
     * Добавление 'пользователя'
     *
     * @param data {@link User} набор полей для создания
     * @return 'пользователь' {@link User} с созданным идентификатором
     */
    @Override
    public User add(User data) {
        var userId = getNewId();
        var result = checkConsistencyAndGetItem(data, userId);
        result.ifPresent(r -> {
            log.warn(ENTITY_CREATE_ERROR.concat(USER_ALREADY_EXIST).concat(userId.toString()));
            throw new EntityAlreadyExistsException(
                    thisService,
                    ENTITY_CREATE_ERROR,
                    USER_ALREADY_EXIST.concat(userId.toString()));
        });
        users.put(userId, new User(userId, data.getName(), data.getEmail()));
        return new User(userId, data.getName(), data.getEmail());
    }


    /**
     * Обновление существующего 'пользователя'
     *
     * @param data {@link User} набор необходимых полей для обновления
     * @param userId идентификатор 'пользователя'
     * @return обновленный 'пользователь' {@link User}
     */
    @Override
    public User update(User data, Long userId) {
        var user = checkConsistencyAndGetItem(data, userId)
                .orElseGet(() -> {
                    log.warn(ENTITY_UPDATE_ERROR.concat(USER_NOT_FOUND).concat(userId.toString()));
                    throw new EntityNotFoundException(
                            thisService, ENTITY_UPDATE_ERROR, USER_NOT_FOUND.concat(userId.toString()));
                });
        Optional.ofNullable(data.getName()).ifPresent(user::setName);
        Optional.ofNullable(data.getEmail()).ifPresent(user::setEmail);
        return new User(
                userId,
                user.getName(),
                user.getEmail());
    }

    /**
     * Удаление 'пользователя'
     *
     * @param userId идентификатор 'пользователя'
     */
    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    private Long getNewId() {
        return ++idCount;
    }

    /**
     * Метод используется для оптимизации создания или обновления элемента в наборе данных за один проход.<p>
     * 1) выполняется проверка уникальных полей в наборе, если значение уже имеется - выбрасывается исключение<p>
     * 2) возвращается искомый элемент из набора данных по его ID для обновления, или проверки на уже существующий
     * элемент при создании.
     *
     * @param data набор данных для поиска 'уникальных' значений
     * @param userId идентификатор 'пользователя', которого нужно получить из репозитория
     * @return 'пользователь' {@link User}
     */
    private Optional<User> checkConsistencyAndGetItem(User data, Long userId) {
        String email = data.getEmail();
        var result = users.values()
                .stream()
                .peek(user -> {
                    if (user.getEmail().equals(email)) {
                        throw new EntityAlreadyExistsException(thisService, email, EMAIL_EXIST);
                    }
                })
                .filter(user -> user.getId().equals(userId))
                .toList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

}
