package ru.practicum.shareit.model.user;

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
import ru.practicum.shareit.model.user.dto.UserMapper;

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
    static final String USER_ID = "user-id";
    UserMapper mapper;
    UserService users;

    @GetMapping("/{user-id}")
    public UserDto get(@PathVariable(name = USER_ID) Long userId) {
        log.info("Запрос GET: получить пользователя ID[{}]", userId);
        var response = users.get(userId);
        log.info(RESPONSE_OK, response.toString());
        return mapper.toUserDto(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        log.info("Запрос POST: создать пользователя {}", userDto.toString());
        var response = users.add(mapper.toAddUser(userDto));
        log.info(RESPONSE_CREATED, response.toString());
        return mapper.toUserDto(response);
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable(name = USER_ID) Long userId) {
        log.info("Запрос PATCH: обновить поля {} у пользователя ID[{}]", userId, userDto.toString());
        var response = users.update(mapper.toUpdateUser(userDto), userId);
        log.info(RESPONSE_OK, response.toString());
        return mapper.toUserDto(response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable(name = USER_ID) Long userId) {
        log.info("Запрос DELETE удалить пользователя ID[{}]", userId);
        users.delete(userId);
        log.info(RESPONSE_NO_CONTENT);
    }

}
