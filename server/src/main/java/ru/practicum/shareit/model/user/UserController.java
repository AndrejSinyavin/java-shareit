package ru.practicum.shareit.model.user;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;
import ru.practicum.shareit.validation.CustomEntityValidator;

/**
 * Контроллер обработки REST-запросов для работы с 'пользователями'
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    static String RESPONSE_OK = "Ответ: '200 OK' {} ";
    static String RESPONSE_CREATED = "Ответ: '201 Created' {} ";
    static String RESPONSE_NO_CONTENT = "Ответ: '204 No Content'";
    static String POST_REQUEST = "Запрос POST: создать пользователя {}";
    static String GET_REQUEST = "Запрос GET: получить пользователя ID[{}]";
    static String PATCH_REQUEST = "Запрос PATCH: обновить поля {} у пользователя ID[{}]";
    static String DELETE_REQUEST = "Запрос DELETE удалить пользователя ID[{}]";
    static final String USER_ID = "user-id";
    UserMapper userMapper;
    CustomEntityValidator entityValidator;
    UserService userService;

    @GetMapping("/{user-id}")
    public UserDto get(@PathVariable(name = USER_ID)
                           @Positive(message = "ID не может быть отрицательным значением")
                           Long userId) {
        log.info(GET_REQUEST, userId);
        var response = userMapper.toUserDto(userService.get(userId));
        log.info(RESPONSE_OK, response.toString());
        return response;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@RequestBody UserDtoCreate user) {
        log.info(POST_REQUEST, user.toString());
        entityValidator.validate(user);
        var response = userMapper.toUserDto(userService.add(userMapper.toAddUser(user)));
        log.info(RESPONSE_CREATED, response.toString());
        return response;
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@RequestBody UserDtoUpdate user,
                          @PathVariable(name = USER_ID)
                          @Positive(message = "ID не может быть отрицательным значением")
                          Long userId) {
        log.info(PATCH_REQUEST, userId, user.toString());
        entityValidator.validate(user);
        var response = userMapper.toUserDto(userService.update(userMapper.toUpdateUser(user), userId));
        log.info(RESPONSE_OK, response.toString());
        return (response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable(name = USER_ID)
                           @Positive(message = "ID не может быть отрицательным значением")
                           Long userId) {
        log.info(DELETE_REQUEST, userId);
        userService.delete(userId);
        log.info(RESPONSE_NO_CONTENT);
    }

}
