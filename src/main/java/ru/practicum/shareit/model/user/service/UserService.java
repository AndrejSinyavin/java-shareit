package ru.practicum.shareit.model.user.service;

import ru.practicum.shareit.model.user.dto.UserDto;

/**
 * Интерфейс сервисов для работы с пользователем
 */
public interface UserService {

    /**
     * Метод возвращает пользователя
     *
     * @param id идентификатор пользователя
     * @return искомый пользователь
     */
    UserDto get(Long id);

    /**
     * Метод добавляет пользователя
     *
     * @param userDto добавляемый пользователь
     * @return пользователь с установленным идентификатором
     */
    UserDto add(UserDto userDto);

    /**
     * Метод обновляет существующего пользователя
     *
     * @param userDto набор необходимых полей для обновления
     * @param id идентификатор пользователя
     * @return обновленный пользователь
     */
    UserDto update(UserDto userDto, Long id);

    /**
     * Метод удаляет пользователя
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);

}
