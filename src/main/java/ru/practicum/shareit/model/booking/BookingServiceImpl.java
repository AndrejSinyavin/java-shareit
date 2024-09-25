package ru.practicum.shareit.model.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityRuntimeErrorException;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.item.ItemRepository;
import ru.practicum.shareit.model.user.UserRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.model.booking.BookingStatus.*;

/**
 * Реализация интерфейса {@link BookingService} для работы с 'бронированием'
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    static String ID = "ID ";
    static String ACCESS_DENIED = "Отказано в доступе";
    static String ITEM_NOT_FOUND = "'Предмет' не найден в репозитории";
    static String ITEM_UNAVAILABLE = "'Предмет' недоступен для бронирования";
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    static String USER_NOT_HAVE_ITEMS = "Не найдено вещей у пользователя";
    static String BOOKING_NOT_FOUND = "Запрос на 'бронирование' не найден в репозитории";
    static String USER_NOT_OWNER = "Пользователь не является владельцем 'предмета' ";
    static String USER_NOT_BOOKER = "или не создавал этот запрос на бронирование";
    static String SAME_BOOKING_DATES = "Дата завершения бронирования не может совпадать с датой начала";
    String thisService = this.getClass().getSimpleName();
    ItemRepository items;
    UserRepository users;
    BookingRepository bookings;

    /**
     * Бронирование вещи
     * @param data шаблон для {@link Booking}
     * @param itemId идентификатор 'вещи', которую хотят 'забронировать'
     * @param bookerId идентификатор 'пользователя', который сделал запрос на 'бронирование'
     * @return сформированный {@link Booking} с установленным идентификатором
     */
    @Override
    public Booking add(Booking data, Long itemId, Long bookerId) {
        String itemSid = ID.concat(itemId.toString());
        String bookerSid = ID.concat(bookerId.toString());
        if (data.getStart().equals(data.getEnd())) {
            throw new EntityRuntimeErrorException(thisService, SAME_BOOKING_DATES);
        }
        var booker = users.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException(thisService, USER_NOT_FOUND, bookerSid));
        var item = items.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(thisService, ITEM_NOT_FOUND, itemSid));
        if (!item.getAvailable()) {
            throw new EntityRuntimeErrorException(thisService, ITEM_UNAVAILABLE, itemSid);
        }
        data.setBooker(booker);
        data.setItem(item);
        data.setStatus(WAITING);
        return bookings.save(data);
    }

    /**
     * Подтверждение/отклонение запроса на аренду вещи
     *
     * @param bookingId идентификатор запроса на аренду
     * @param itemOwnerId владелец вещи
     * @param approved подтверждение/отклонение запроса на аренду вещи
     * @return откорректированный входящий запрос на аренду с измененным владельцем статусом запроса
     */
    @Override
    public Booking approve(Long bookingId, Long itemOwnerId, Boolean approved) {
        var owner = users.findById(itemOwnerId).orElseThrow(() ->
                        new EntityRuntimeErrorException(
                                thisService, USER_NOT_FOUND, ID.concat(itemOwnerId.toString()))
        );
        var booking = bookings.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(thisService, BOOKING_NOT_FOUND, ID.concat(bookingId.toString()))
        );
        if (!Objects.equals(booking.getItem().getOwner().getId(), owner.getId())) {
            throw new EntityAccessDeniedException(
                    thisService,
                    USER_NOT_OWNER,
                    ID.concat(booking.getItem().getId().toString())
            );
        }
        if (approved) {
            if (booking.getStatus().equals(WAITING) && booking.getItem().getAvailable()) {
                booking.setStatus(APPROVED);
                booking.getItem().setAvailable(false);
                return bookings.save(booking);
            } else {
                return booking;
            }
        } else {
            if (booking.getStatus().equals(WAITING)) {
                booking.setStatus(REJECTED);
                return bookings.save(booking);
            } else {
                return booking;
            }
        }
    }

    /**
     * Получить информацию о запросе на бронирование автору запроса или владельцу вещи
     *
     * @param bookingId идентификатор запроса
     * @param userId идентификатор автора запроса или владельца вещи
     * @return информация о запросе на аренду
     */
    @Override
    public Booking getBooking(Long bookingId, Long userId) {
        if (!users.existsById(userId)) {
            throw new EntityRuntimeErrorException(thisService, USER_NOT_FOUND, ID.concat(userId.toString()));
        }
        var booking = bookings.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(thisService, BOOKING_NOT_FOUND, ID.concat(bookingId.toString()))
        );
        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();
        if (!Objects.equals(userId, bookerId) && !Objects.equals(userId, ownerId)) {
            throw new EntityAccessDeniedException(thisService, ACCESS_DENIED, USER_NOT_OWNER.concat(USER_NOT_BOOKER));
        } else {
            return booking;
        }
    }

    /**
     * Получить информацию о всех запросах на бронирование, сделанных пользователем
     *
     * @param userId идентификатор пользователя
     * @param state фильтр для выбора категории состояний запросов
     * @return список запросов по выбранной категории
     */
    @Override
    public Collection<Booking> getBookingsByUser(Long userId, BookingSearchCriteria state) {
        if (!users.existsById(userId)) {
            throw new EntityRuntimeErrorException(thisService, USER_NOT_FOUND, ID.concat(userId.toString()));
        }
        return switch (state) {
            case ALL -> bookings.findAllByBookerIdIsOrderByStartDesc(userId);
            case WAITING -> bookings.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, WAITING);
            case REJECTED -> bookings.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, REJECTED);
            case CURRENT -> bookings.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, APPROVED);
            case FUTURE -> bookings.findAllByBookerIdIsAndStartAfterOrderByStartDesc(userId, Instant.now());
            case PAST -> bookings.findAllByBookerIdIsAndEndBeforeOrderByStartDesc(userId, Instant.now());
        };
    }

    /**
     * Получить информацию о запросах на бронирование по каждой вещи конкретного владельца.
     *
     * @param ownerId идентификатор пользователя
     * @param state фильтр для выбора категории состояний запросов
     * @return список запросов по выбранной категории
     */
    @Override
    public Collection<Booking> getBookingsByAllUserItems(Long ownerId, BookingSearchCriteria state) {
        if (!users.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, ID.concat(ownerId.toString()));
        }
        Set<Long> itemIds = items.getItemsByOwnerId(ownerId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toSet());
        if (itemIds.isEmpty()) {
            throw new EntityAccessDeniedException(thisService, ID.concat(ownerId.toString()), USER_NOT_HAVE_ITEMS);
        }
        return switch (state) {
            case ALL -> bookings.findAllByBookerIdIsAndItemIdInOrderByStartDesc(
                    ownerId, itemIds);
            case WAITING -> bookings.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    ownerId, itemIds, WAITING);
            case REJECTED -> bookings.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    ownerId, itemIds, REJECTED);
            case CURRENT -> bookings.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    ownerId, itemIds, APPROVED);
            case FUTURE -> bookings.findAllByBookerIdIsAndItemIdInAndStartAfterOrderByStartDesc(
                    ownerId, itemIds, Instant.now());
            case PAST -> bookings.findAllByBookerIdIsAndItemIdInAndEndBeforeOrderByStartDesc(
                    ownerId, itemIds, Instant.now());
        };
    }

}
