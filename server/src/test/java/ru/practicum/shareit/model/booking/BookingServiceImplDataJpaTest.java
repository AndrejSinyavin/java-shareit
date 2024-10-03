package ru.practicum.shareit.model.booking;

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
import ru.practicum.shareit.model.user.UserMapperImpl;
import ru.practicum.shareit.model.user.UserService;
import ru.practicum.shareit.model.user.UserServiceImpl;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;

import java.time.Clock;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@Transactional
@Import(value = {ItemServiceImpl.class, BookingServiceImpl.class, UserServiceImpl.class, ItemMapperImpl.class,
        UserMapperImpl.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Набор интеграционных тестов сервиса 'бронирование вещей'")
class BookingServiceImplDataJpaTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private Long userId;
    private Long bookerId;
    private Item item;
    private Booking booking;
    private User user;
    private User booker;

    @BeforeEach
    void setUpData() {
        userId = userService.add(new UserDtoCreate( "user", "user@yandex.ru")).id();
        user = new User(userId, "user", "user@yandex.ru");
        bookerId = userService.add(new UserDtoCreate( "booker", "booker@yandex.ru")).id();
        booker = new User(bookerId, "booker", "booker@yandex.ru");
        item = itemService.add(
                new Item(0L, "item", "desc", true, user, null), userId);
        booking = new Booking(
                0L,
                Instant.now(Clock.systemUTC()).minus(1, DAYS),
                Instant.now(Clock.systemUTC()).plus(1, DAYS),
                item,
                new User(bookerId, "booker", "booker@yandex.ru"),
                BookingStatus.WAITING);
    }

    @Test
    @DisplayName("Сценарий, тестирующий создание запроса на бронирование предмета в репозитории")
    void addTest() {
        var bookingId = bookingService.add(booking, item.getId(), bookerId).getId();
        var testBooking = bookingService.getBooking(item.getId(), bookerId);
        assertThat(testBooking, is(notNullValue()));
        assertThat(testBooking.getId(), is(notNullValue()));
        assertThat(testBooking.getId(), is(bookingId));
        assertThat(testBooking.getItem(), is(item));
        assertThat(testBooking.getStatus(), is(booking.getStatus()));
        assertThat(testBooking.getStart(), is(booking.getStart()));
        assertThat(testBooking.getEnd(), is(booking.getEnd()));
    }

    @Test
    @DisplayName("Сценарий, тестирующий подтверждение запроса на бронирование предмета его владельцем")
    void approveTest() {
        addTest();
        var temp = bookingService.getBookingsByUser(bookerId, BookingSearchCriteria.WAITING);
        Long bookingId = temp.stream().toList().getFirst().getId();
        bookingService.approve(bookingId, userId, true);
        var testBooking = bookingService.getBooking(bookingId, bookerId);
        assertThat(testBooking, is(notNullValue()));
        assertThat(testBooking.getId(), is(bookingId));
        assertThat(testBooking.getStatus(), is(BookingStatus.APPROVED));
        assertThat(testBooking.getItem(), is(item));
    }

    @Test
    @DisplayName("Сценарий, тестирующий чтение запроса на бронирование предмета из репозитория")
    void getBookingTest() {
        addTest();
    }

    @Test
    void getBookingsByUserTest() {
        approveTest();
        var itemTwo = new Item(0L, "item2", "desc2", true, user, null);
        Long itemIdTwo = itemService.add( itemTwo, userId).getId();
        Long bookingIdTwo = bookingService.add(
                new Booking(
                        0L,
                        Instant.now().plus(1, DAYS),
                        Instant.now().plus(2, DAYS),
                        itemTwo,
                        booker,
                        BookingStatus.WAITING),
                itemIdTwo,
                booker.getId()
        ).getId();
        var listBookings = bookingService.getBookingsByUser(booker.getId(), BookingSearchCriteria.ALL);
        assertThat(listBookings.size(), is(2));
        var test = listBookings.stream().toList();
        assertThat(test.getFirst().getId(), is(bookingIdTwo));
        assertThat(test.getFirst().getStatus(), is(BookingStatus.WAITING));
        assertThat(test.getLast().getId(), is(bookingIdTwo - 1));
        assertThat(test.getLast().getStatus(), is(BookingStatus.APPROVED));
    }

    @Test
    void getBookingsByAllUserItemsTest() {
        getBookingsByUserTest();
        var itemThree = new Item(0L, "item3", "desc3", true, user, null);
        Long itemThreeId = itemService.add(itemThree, userId).getId();
        bookingService.add(
                new Booking(
                        0L,
                        Instant.now().plus(1, DAYS),
                        Instant.now().plus(2, DAYS),
                        itemThree,
                        booker,
                        BookingStatus.WAITING
                ),
                itemThreeId,
                booker.getId()
        );
        var usersItemsList = bookingService.getBookingsByAllUserItems(user.getId(), BookingSearchCriteria.ALL);
        var testList = usersItemsList.stream().toList();
        assertThat(testList.size(), is(3));
        assertThat(testList.getFirst().getItem().getName(), is("item3"));
        assertThat(testList.getFirst().getStatus(), is(BookingStatus.WAITING));
        assertThat(testList.get(1).getItem().getName(), is("item2"));
        assertThat(testList.get(1).getStatus(), is(BookingStatus.WAITING));
        assertThat(testList.get(2).getItem().getName(), is("item"));
        assertThat(testList.get(2).getStatus(), is(BookingStatus.APPROVED));
    }
}