package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService users;

    @GetMapping
    public UserDto getUser(@RequestParam Long userId) {
        return users.getUser(userId);
    }

    @PostMapping
    public UserDto createUser(UserDto user) {
        return users.createUser(user);
    }

    @PutMapping
    public UserDto updateUser(UserDto user) {
        return users.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam Long userId) {
        users.deleteUser(userId);
    }

}
