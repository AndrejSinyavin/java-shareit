package ru.practicum.shareit.model.booking;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.model.booking.dto.BookingDto;
import ru.practicum.shareit.model.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Collection;
import java.util.Optional;

import static ru.practicum.shareit.model.booking.BookingSearchCriteria.*;

/**
 * Контроллер обработки REST-запросов для работы с 'бронированием вещей'
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookingController {
    static String RESPONSE_OK = "Ответ: '200 OK' {} ";
    static String RESPONSE_CREATED = "Ответ: '201 Created' {} ";
    static String POST_REQUEST = "Запрос POST: создать запрос от 'пользователя' ID[{}] на бронирование вещи ID[{}]";
    static String PATCH_REQUEST =
            "Запрос PATCH: принять/отклонить запрос на бронирование ID[{}], владелец ID[{}], статус решения {}";
    static String GET_BOOKING = "Запрос GET: получить для просмотра запрос на бронирование ID[{}] пользователю ID[{}]";
    static String GET_BOOKINGS = "Запрос GET: получить для просмотра список всех запросов на бронирование " +
            "пользователем ID[{}] по условию STATE = [{}]";
    static String USER_UNDEFINED = "Не указан ID пользователя, создавшего запрос на сервер";
    static String BOOKER_ID = "Создатель запроса на бронирование с ID[{}]";
    static String ABSENT_HEADER = "Отсутствует заголовок ";
    static String BOOKING_STATE_WRONG = "Не распознан статус бронирования предмета ";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String APPROVED = "approved";
    static final String STATE = "state";
    static final String BOOKING_ID = "booking-id";
    static final String ALL = "ALL";
    String thisService = this.getClass().getSimpleName();
    CustomEntityValidator entityValidator;
    BookingMapper bookingMapper;
    BookingService bookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDtoCreate booking,
                                    @RequestHeader(value = HEADER_SHARER, required = false)
                                    @Positive(message = "ID 'пользователя' должен быть больше нуля")
                                    Long bookerId)  {
        log.info(POST_REQUEST, bookerId, booking.itemId());
        checkSharerHeader(bookerId);
        entityValidator.validate(booking);
        var response = bookingMapper.toBookingDto(
                bookingService.add(bookingMapper.toBooking(booking), booking.itemId(), bookerId)
        );
        log.info(RESPONSE_CREATED.concat(BOOKER_ID), response.toString(), bookerId);
        return response;
    }

    @PatchMapping("/{booking-id}")
    public BookingDto confirmBooking(@PathVariable(value = BOOKING_ID)
                                     @Positive(message = "ID 'бронирования' должен быть положительным значением")
                                     Long bookingId,
                                     @RequestHeader(value = HEADER_SHARER, required = false)
                                     @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                     Long ownerId,
                                     @RequestParam(value = APPROVED)
                                     Boolean approved
                                     ) {
        log.info(PATCH_REQUEST, bookingId, ownerId, approved);
        checkSharerHeader(ownerId);
        var response = bookingMapper.toBookingDto(bookingService.approve(bookingId, ownerId, approved));
        log.info(RESPONSE_OK, response.toString());
        return response;
    }

    @GetMapping("/{booking-id}")
    public BookingDto getBooking(@PathVariable(value = BOOKING_ID)
                                 @Positive(message = "ID 'бронирования' должен быть положительным значением")
                                 Long bookingId,
                                 @RequestHeader(value = HEADER_SHARER)
                                 @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                 Long userId) {
        log.info(GET_BOOKING, bookingId, userId);
        checkSharerHeader(userId);
        var response = bookingMapper.toBookingDto(bookingService.getBooking(bookingId, userId));
        log.info(RESPONSE_OK, response.toString());
        return response;
    }

    @GetMapping
    public Collection<BookingDto> getBookingsByUser(@RequestHeader(value = HEADER_SHARER, required = false)
                                          @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                          Long userId,
                                          @RequestParam(value = STATE, defaultValue = "ALL")
                                          String state) {
        log.info(GET_BOOKINGS, userId, state);
        checkSharerHeader(userId);
        var response = bookingService.getBookingsByUser(userId, checkState(state))
                .stream()
                .map(bookingMapper::toBookingDto)
                .toList();
        log.info(RESPONSE_OK, response);
        return response;
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsForOwnerItems(@RequestHeader(value = HEADER_SHARER, required = false)
                                           @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                           Long ownerId,
                                           @RequestParam(value = STATE, defaultValue = ALL)
                                           String state) {
        checkSharerHeader(ownerId);
        var response = bookingService.getBookingsByAllUserItems(ownerId, checkState(state))
                .stream()
                .map(bookingMapper::toBookingDto)
                .toList();
        log.info(RESPONSE_OK, response);
        return response;
    }

    private void checkSharerHeader(Long userId) {
        Optional.ofNullable(userId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, USER_UNDEFINED, ABSENT_HEADER.concat(HEADER_SHARER)
                )
        );
    }

    private BookingSearchCriteria checkState(String state) {
        try {
            return valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new EntityValidateException(thisService, BOOKING_STATE_WRONG, state);
        }
    }

}
