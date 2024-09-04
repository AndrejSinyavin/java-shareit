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
    static final String USER_NOT_FOUND = "Пользователь с указанным ID не найден";
    static final String EMAIL_EXIST = "Пользователь с таким email уже существует";
    static final String EMPTY = "";
    final String thisService = this.getClass().getSimpleName();
    final Map<Long, User> users = new HashMap<>();
    Long idCount = 0L;

    /**
     * Получение пользователя
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @Override
    public User get(Long id) {
        var user = users.get(id);
        if (user == null) {
            log.warn(USER_NOT_FOUND);
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, EMPTY);
        } else {
            return new User(id, user.getName(), user.getEmail());
        }
    }

    /**
     * Добавление пользователя
     *
     * @param data набор полей для создания
     * @return пользователь с созданным идентификатором
     */
    @Override
    public User add(User data) {
        var id = getNewId();
        checkConsistency(data);
        users.put(id, new User(id, data.getName(), data.getEmail()));
        return new User(id, data.getName(), data.getEmail());
    }


    /**
     * Обновление существующего пользователя
     *
     * @param data набор необходимых полей для обновления
     * @return обновленный пользователь
     */
    @Override
    public User update(User data, Long id) {
        if (!users.containsKey(id)) {
            log.warn(USER_NOT_FOUND);
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, EMPTY);
        }
        checkConsistency(data);
        User user = users.get(id);
        Optional.ofNullable(data.getName()).ifPresent(user::setName);
        Optional.ofNullable(data.getEmail()).ifPresent(user::setEmail);
        return new User(id, user.getName(), user.getEmail());
    }

    /**
     * Удаление существующего пользователя
     *
     * @param id идентификатор пользователя в репозитории
     */
    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private Long getNewId() {
        return ++idCount;
    }

    private void checkConsistency(User data) {
        String email = data.getEmail();
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
            throw new EntityAlreadyExistsException(thisService, email, EMAIL_EXIST);
        }
    }

}
