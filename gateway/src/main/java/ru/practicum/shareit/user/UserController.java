package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserDtoCreate;
import ru.practicum.shareit.user.dto.UserDtoUpdate;
import ru.practicum.shareit.validation.CustomEntityValidator;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    static final String USER_ID = "user-id";
    static final String RESPONSE = "Response: {}";
    CustomEntityValidator entityValidator;
    UserClient userClient;

    @PostMapping
    public Object addUser(@RequestBody @Valid UserDtoCreate user) {
        entityValidator.validate(user);
        var response = userClient.add(user);
        log.info(RESPONSE, response);
        return response;
    }

    @PatchMapping("/{user-id}")
    public Object updateUser(@RequestBody @Valid UserDtoUpdate user,
                             @PathVariable(name = USER_ID)
                             @Positive(message = "ID не может быть отрицательным значением")
                             long userId) {
        entityValidator.validate(user);
        var response = userClient.update(user, userId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping("/{user-id}")
    public Object getUser(@PathVariable(name = USER_ID)
                          @Positive(message = "ID не может быть отрицательным значением")
                          long userId) {
        var response = userClient.get(userId);
        log.info(RESPONSE, response);
        return response;
    }

    @DeleteMapping("/{user-id}")
    public Object delete(@PathVariable(name = USER_ID)
                       @Positive(message = "ID не может быть отрицательным значением")
                       long userId) {
        var response = userClient.delete(userId);
        log.info(RESPONSE, response);
        return response;
    }

}
