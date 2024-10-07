package ru.practicum.shareit.model.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityRuntimeErrorException;
import ru.practicum.shareit.model.booking.Booking;
import ru.practicum.shareit.model.booking.BookingService;
import ru.practicum.shareit.model.booking.BookingServiceImpl;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.item.dto.CommentDtoCreate;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.model.user.UserMapperImpl;
import ru.practicum.shareit.model.user.UserService;
import ru.practicum.shareit.model.user.UserServiceImpl;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@Transactional
@Import(value = {ItemServiceImpl.class, BookingServiceImpl.class, UserServiceImpl.class, ItemMapperImpl.class,
        UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Набор интеграционных тестов сервиса 'работы с вещами'")
class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private Long userId;
    private Long bookerId;

    @BeforeEach
    void setUpData() {
        userId = userService.add(new UserDtoCreate("user", "user@yandex.ru")).id();
        bookerId = userService.add(new UserDtoCreate("booker", "booker@yandex.ru")).id();
    }

    @Test
    @DisplayName("Сценарий, тестирующий создание предмета в репозитории")
    void addTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var userItems = itemService.getItemsByOwner(userId);
        assertThat("Список получен", userItems, is(notNullValue()));
        assertThat("У пользователя нет собственных предметов", userItems.isEmpty(), is(true));
        var item = itemService.add(newItem, userId);
        assertThat("Новый предмет корректно создан", item, is(notNullValue()));
        assertThat("Название предмета правильное", item.getName(), is("item"));
        assertThat("Описание предмета правильное", item.getDescription(), is("description"));
        assertThat("Пользователь является владельцем этого предмета", item.getOwner().getId(), is(userId));
        var itemList = itemService.getItemsByOwner(userId).stream().toList();
        assertThat("Пользователь владеет всего 1 предметом,", itemList.size() == 1, is(true));
        assertThat("и именно этим предметом", itemList.getFirst().getId(), is(item.getId()));
        userService.delete(userId);
        Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.add(newItem, userId));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение предмета из репозитория")
    void getTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var itemId = itemService.add(newItem, userId).getId();
        var item = itemService.get(itemId);
        assertThat("Новый предмет корректно создан", item, is(notNullValue()));
        assertThat("Идентификатор предмета правильный", item.getId(), is(itemId));
        assertThat("Название предмета правильное", item.getName(), is("item"));
        assertThat("Описание предмета правильное", item.getDescription(), is("description"));
    }

    @Test
    @DisplayName("Сценарий, тестирующий обновление полей предмета")
    void updateItemTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var updateItem = new Item(
                0L, "update", "update", true, null, null);
        Long itemId = itemService.add(newItem, userId).getId();
        var testItem = itemService.get(itemId);
        assertThat(testItem, is(notNullValue()));
        assertThat(testItem.getId(), is(itemId));
        assertThat(testItem.getName(), is(newItem.getName()));
        assertThat(testItem.getDescription(), is(newItem.getDescription()));
        assertThat(testItem.getAvailable(), is(newItem.getAvailable()));
        var updatedItem = itemService.update(updateItem, itemId, userId);
        assertThat(updatedItem, is(notNullValue()));
        assertThat(updatedItem.getId(), is(itemId));
        assertThat(updatedItem.getName(), is(updateItem.getName()));
        assertThat(updatedItem.getDescription(), is(updateItem.getDescription()));
        assertThat(updatedItem.getAvailable(), is(updateItem.getAvailable()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий обновление полей предмета - пользователь не существует")
    void updateItemWithoutUserTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var updateItem = new Item(
                0L, "update", "update", true, null, null);
        Long itemId = itemService.add(newItem, userId).getId();
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.update(updateItem, itemId, 999L));
    }

    @Test
    @DisplayName("Сценарий, тестирующий обновление полей предмета - предмет не существует")
    void updateItemWithoutItemTest() {
        var updateItem = new Item(
                0L, "update", "update", true, null, null);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.update(updateItem, 999L, userId));
    }

    @Test
    @DisplayName("Сценарий, тестирующий обновление полей предмета - пользователь обновляет не свой предмет")
    void updateNotYourItemTest() {
        var otherUserId = userService.add(new UserDtoCreate("other", "other@yandex.ru")).id();
        var newItem = new Item(0L, "item", "description", false, null, null);
        var updateItem = new Item(
                0L, "update", "update", true, null, null);
        Long itemId = itemService.add(newItem, userId).getId();
        Assertions.assertThrows(EntityAccessDeniedException.class,
                () -> itemService.update(updateItem, itemId, otherUserId));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение списка всех предметов конкретного пользователя")
    void getItemsByOwnerTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var userItems = itemService.getItemsByOwner(userId);
        assertThat("Список получен", userItems, is(notNullValue()));
        assertThat("У пользователя нет собственных предметов", userItems.isEmpty(), is(true));
        var item = itemService.add(newItem, userId);
        var result = itemService.getItemsByOwner(userId).stream().toList();
        assertThat("Список получен", result, is(notNullValue()));
        assertThat("У пользователя есть только один предмет", result.size(), is(1));
        var itemDto = result.getFirst();
        assertThat("", itemDto.getName(), is(item.getName()));
        assertThat("", itemDto.getDescription(), is(item.getDescription()));
        assertThat(item.getAvailable(), is(item.getAvailable()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение списка всех предметов - пользователя не существует")
    void getItemsNotExistOwnerTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.getItemsByOwner(9999L));
    }

    @Test
    @DisplayName("Сценарий, тестирующий получение списка предметов конкретного пользователя")
    void getItemsByOwnerDateTest() {
        var newItem = new Item(0L, "item", "description", false, null, null);
        var userItems = itemService.getItemsByOwner(userId);
        assertThat("Список получен", userItems, is(notNullValue()));
        assertThat("У пользователя нет собственных предметов", userItems.isEmpty(), is(true));
        var item = itemService.add(newItem, userId);
        var result = itemService.getItemsByOwner(userId).stream().toList();
        assertThat("Список получен", result, is(notNullValue()));
        assertThat("У пользователя есть только один предмет", result.size(), is(1));
        var itemDto = result.getFirst();
        assertThat("", itemDto.getName(), is(item.getName()));
        assertThat("", itemDto.getDescription(), is(item.getDescription()));
        assertThat(item.getAvailable(), is(item.getAvailable()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий поиск предметов, имеющих искомый шаблон в имени или описании," +
            " и предмет доступен для аренды")
    void searchTest() {
        var firstId = itemService.add(
                new Item(0L, "itemSEARCHany", "descONE", true, null, null),
                userId).getId();
        var secondId = itemService.add(
                new Item(0L, "item", "descONE", true, null, null),
                userId).getId();
        itemService.add(
                new Item(0L, "item", "description", false, null, null),
                userId);
        var result = itemService.search("sEarCh");
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(1));
        assertThat(result.stream().toList().getFirst().getId(), is(firstId));
        result = itemService.search("OnE");
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
        var list = result.stream().toList();
        assertThat(list.getFirst().getId(), is(firstId));
        assertThat(list.getLast().getId(), is(secondId));
        result = itemService.search("!!!");
        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
        result = itemService.search("item");
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету")
    void addCommentTest() {
        var item = new Item(0L, "item", "desc", true, null, null);
        Long itemId = itemService.add(
                item,
                userId).getId();
        var listComments = itemService.get(itemId).getComments();
        assertThat(listComments.isEmpty(), is(true));
        User booker = new User(bookerId,"booker", "booker@yandex.ru");
        var booking = bookingService.add(
                new Booking(
                        0L,
                        Instant.now(Clock.systemUTC()).minus(2, ChronoUnit.DAYS),
                        Instant.now(Clock.systemUTC()).minus(1, ChronoUnit.DAYS),
                        item,
                        booker,
                        BookingStatus.WAITING),
                itemId, bookerId);
        bookingService.approve(booking.getId(), userId, true);
        itemService.addComment(bookerId, itemId,
                new CommentDtoCreate("Ура!"));
        listComments = itemService.get(itemId).getComments();
        assertThat(listComments.size(), is(1));
        assertThat(listComments.getFirst().authorName(), is(booker.getName()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету, предмет не существует")
    void addCommentWithNotAvailableItemTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.addComment(bookerId, 9999L,
                        new CommentDtoCreate("Ура!"))
        );
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету, автор коммента не существует")
    void addCommentWithNotAvailableAuthorTest() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.addComment(999L, 1L,
                        new CommentDtoCreate("Ура!"))
        );
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету, на предмет не было запросов на аренду")
    void addCommentWithoutBookingTest() {
        var item = new Item(0L, "item", "desc", true, null, null);
        Long itemId = itemService.add(item, userId).getId();
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> itemService.addComment(bookerId, itemId,
                        new CommentDtoCreate("Ура!"))
        );
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету, пользователь не получал approve от владельца")
    void addCommentWithoutApproveTest() {
        var item = new Item(0L, "item", "desc", true, null, null);
        Long itemId = itemService.add(
                item,
                userId).getId();
        var listComments = itemService.get(itemId).getComments();
        assertThat(listComments.isEmpty(), is(true));
        User booker = new User(bookerId,"booker", "booker@yandex.ru");
        var booking = bookingService.add(
                new Booking(
                        0L,
                        Instant.now(Clock.systemUTC()).minus(2, ChronoUnit.DAYS),
                        Instant.now(Clock.systemUTC()).minus(1, ChronoUnit.DAYS),
                        item,
                        booker,
                        BookingStatus.WAITING),
                itemId, bookerId);
        Assertions.assertThrows(EntityAccessDeniedException.class,
                () -> itemService.addComment(bookerId, itemId,
                        new CommentDtoCreate("Ура!"))
        );
    }

    @Test
    @DisplayName("Сценарий, тестирующий добавление комментария к предмету, неверные сроки аренды предмета")
    void addCommentWithIncorrectDateTimeTest() {
        var item = new Item(0L, "item", "desc", true, null, null);
        Long itemId = itemService.add(
                item,
                userId).getId();
        var listComments = itemService.get(itemId).getComments();
        assertThat(listComments.isEmpty(), is(true));
        User booker = new User(bookerId,"booker", "booker@yandex.ru");
        var booking = bookingService.add(
                new Booking(
                        0L,
                        Instant.now(Clock.systemUTC()).plus(1, ChronoUnit.DAYS),
                        Instant.now(Clock.systemUTC()).plus(2, ChronoUnit.DAYS),
                        item,
                        booker,
                        BookingStatus.WAITING),
                itemId, bookerId);
        bookingService.approve(booking.getId(), userId, true);
        Assertions.assertThrows(EntityRuntimeErrorException.class,
                () -> itemService.addComment(bookerId, itemId,
                        new CommentDtoCreate("Ура!"))
        );
    }

}