package ru.practicum.shareit.model.user.controller;

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
import ru.practicum.shareit.model.user.service.UserService;

/**
 * Контроллер обработки REST-запросов для работы с пользователями
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService users;

    @GetMapping("/{user-id}")
    public UserDto get(@PathVariable(name = "user-id") Long userId) {
        log.info("Запрос => GET user ID[{}]", userId);
        var user = users.get(userId);
        log.info("Ответ <= '200 OK' {}", user);
        return user;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        log.info("Запрос => POST {}", userDto);
        var response = users.add(userDto);
        log.info("Ответ <= '201 Created' {}", response);
        return response;
    }

    @PatchMapping("/{user-id}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable(name = "user-id") Long userId) {
        log.info("Запрос => PATCH user ID[{}] {}", userId, userDto);
        var response = users.update(userDto, userId);
        log.info("Ответ <= '200 OK' {}", response);
        return response;
    }

    @DeleteMapping("/{user-id}")
    public void delete(@PathVariable(name = "user-id") Long userId) {
        log.info("Запрос => DELETE user ID[{}]", userId);
        users.delete(userId);
        log.info("Ответ <= '200 OK'");
    }

}
