package ru.practicum.shareit.model.user;

import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;

/**
 * Интерфейс сервисов для работы с 'пользователями'
 */
public interface UserService {

    /**
     * Метод возвращает 'пользователя'
     *
     * @param id идентификатор 'пользователя'
     * @return искомый 'пользователь'
     */
    UserDto get(Long id);

    /**
     * Метод добавляет 'пользователя'
     *
     * @param user добавляемый 'пользователь'
     * @return 'пользователь' с установленным идентификатором
     */
    UserDto add(UserDtoCreate user);

    /**
     * Метод обновляет существующего 'пользователя'
     *
     * @param user набор необходимых полей для обновления
     * @param id идентификатор целевого 'пользователя'
     * @return обновленный 'пользователь'
     */
    UserDto update(UserDtoUpdate user, Long id);

    /**
     * Метод удаляет 'пользователя'
     *
     * @param id идентификатор 'пользователя'
     */
    void delete(Long id);

}
