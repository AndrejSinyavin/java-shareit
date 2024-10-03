package ru.practicum.shareit.model.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.item.ItemMapperImpl;
import ru.practicum.shareit.model.item.ItemService;
import ru.practicum.shareit.model.item.ItemServiceImpl;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.model.user.UserMapper;
import ru.practicum.shareit.model.user.UserMapperImpl;
import ru.practicum.shareit.model.user.UserService;
import ru.practicum.shareit.model.user.UserServiceImpl;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;

import java.time.Clock;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@Transactional
@Import(value = {ItemServiceImpl.class, UserServiceImpl.class, ItemRequestServiceImpl.class, ItemMapperImpl.class,
        UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Набор интеграционных тестов сервиса 'работы с запросами'")
class ItemRequestServiceImplDataJpaTest {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;
    private final UserMapper mapper;
    private User user;
    private Item item;
    private User booker;
    private ItemRequest request;

    @BeforeEach
    void setUpData() {
        user = mapper.toUser(userService.add(new UserDtoCreate( "user", "user@yandex.ru")));
        booker = mapper.toUser(userService.add(new UserDtoCreate("booker", "booker@yandex.ru")));
        request = itemRequestService.add(
                new ItemRequest(
                        0L,
                        "Дрель, обязательно розовая, жена не любит другой цвет",
                        booker,
                        Instant.now(Clock.systemUTC())),
                booker.getId()
        );
        item = new Item(
                0L,
                "Дрель",
                "Розовая. Осталась от первой жены. Можете оставить себе и не возвращать :)",
                true,
                user,
                request.getId()
        );
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление запроса на добавление предмета")
    void addTest() {
        var userId = user.getId();
        itemService.add(item, userId);
        var target = itemService.search("розовая").stream().findFirst().get();
        assertThat(target, notNullValue());
        assertThat(target.getName(), is("Дрель"));
        assertThat(target.getDescription(), is(
                "Розовая. Осталась от первой жены. Можете оставить себе и не возвращать :)")
        );

    }

    @Test
    @DisplayName("Сценарий, тестирующий получение запроса на аренду предмета")
    void getItemRequestTest() {
        var userId = user.getId();
        var bookerId = booker.getId();
        itemService.add(item, userId).getId();
        var dtoWithAnswer = itemRequestService.getItemRequest(this.request.getId(), bookerId);
        assertThat(dtoWithAnswer, notNullValue());
        assertThat(dtoWithAnswer.getId(), is(request.getId()));
        var suggestion = dtoWithAnswer.getItems().stream().findFirst().get();
        assertThat(suggestion, notNullValue());
        assertThat(suggestion.name(), is(item.getName()));
        assertThat(suggestion.ownerId(), is(userId));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение списка всех запросов пользователя на аренду")
    void getAllOwnersRequestsTest() {
        var userId = user.getId();
        var bookerId = booker.getId();
        itemService.add(item, userId).getId();
        var requestList = itemRequestService.getAllOwnersRequests(bookerId);
        assertThat(requestList, notNullValue());
        assertThat(requestList.size(), is(1));
        assertThat(requestList.getFirst().getId(), is(request.getId()));
        var suggestions = requestList.getLast().getItems().stream().findFirst().get();
        assertThat(suggestions, notNullValue());
        assertThat(suggestions.name(), is(item.getName()));
        assertThat(suggestions.ownerId(), is(userId));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение списка всех запросов всех пользователя на аренду")
    void getAllRequestTest() {
        var userId = user.getId();
        itemService.add(item, userId).getId();
        var requestList = itemRequestService.getAllRequest(userId);
        assertThat(requestList, is(notNullValue()));
        assertThat(requestList.size(), is(1));
        var itemRequest = requestList.stream().findFirst().get();
        assertThat(itemRequest, is(notNullValue()));
        assertThat(itemRequest.getId(), is(request.getId()));
        assertThat(itemRequest.getDescription(), is(request.getDescription()));
    }
}