package ru.practicum.shareit.model.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional
@Import(value = {UserServiceImpl.class, UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Набор интеграционных тестов сервиса 'работы с пользователями'")
class UserServiceImplDataJpaTest {
    private final UserDtoCreate newUser = new UserDtoCreate("user", "user@mail.com");
    private final UserDtoUpdate updateUser = new UserDtoUpdate("newName", "newEmail@mail.com");
    private final UserService userService;

    @Test
    @DisplayName("Сценарий, тестирующий получение пользователя из репозитория")
    void getTest() {
        var user = userService.add(newUser);
        Long id = user.id();
        user = userService.get(id);
        assertThat(user.name(), is(newUser.name()));
        assertThat(user.email(), is(newUser.email()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий создание пользователя в репозитории")
    void addTest() {
        var user = userService.add(newUser);
        Long id = user.id();
        user = userService.get(id);
        assertThat(user.name(), is(newUser.name()));
        assertThat(user.email(), is(newUser.email()));

    }

    @Test
    @DisplayName("Сценарий, тестирующий обновление пользователя в репозитории")
    void updateTest() {
        var user = userService.add(newUser);
        Long id = user.id();
        user = userService.get(id);
        assertThat(user.name(), is(newUser.name()));
        assertThat(user.email(), is(newUser.email()));
        user = userService.update(updateUser, id);
        assertThat(user.name(), is("newName"));
        assertThat(user.email(), is("newEmail@mail.com"));
    }

    @Test
    @DisplayName("Сценарий, тестирующий удаление пользователя из репозитория")
    void deleteTest() {
        var user = userService.add(newUser);
        Long id = user.id();
        user = userService.get(id);
        assertThat(user.name(), is(newUser.name()));
        assertThat(user.email(), is(newUser.email()));
        userService.delete(id);
        assertThrows(EntityNotFoundException.class, () -> userService.get(id));
    }
}