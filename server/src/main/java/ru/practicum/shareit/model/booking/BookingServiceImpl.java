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

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.model.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.model.booking.BookingStatus.WAITING;

/**
 * Реализация интерфейса {@link BookingService} для работы с 'бронированием'
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    static String ID = "ID ";
    static String ITEM_NOT_FOUND = "'Предмет' не найден в репозитории";
    static String ITEM_UNAVAILABLE = "'Предмет' недоступен для бронирования";
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    static String BOOKING_NOT_FOUND = "Запрос на 'бронирование' не найден в репозитории";
    static String USER_NOT_OWNER = "Пользователь не является владельцем 'предмета' ";
    static String SAME_BOOKING_DATES = "Дата завершения бронирования не может совпадать с датой начала";
    String thisService = this.getClass().getSimpleName();
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;

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
        var booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException(thisService, USER_NOT_FOUND, bookerSid));
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(thisService, ITEM_NOT_FOUND, itemSid));
        if (!item.getAvailable()) {
            throw new EntityRuntimeErrorException(thisService, ITEM_UNAVAILABLE, itemSid);
        }
        data.setBooker(booker);
        data.setItem(item);
        data.setStatus(WAITING);
        return bookingRepository.save(data);
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
        var owner = userRepository.findById(itemOwnerId).orElseThrow(() ->
                        new EntityRuntimeErrorException(
                                thisService, USER_NOT_FOUND, ID.concat(itemOwnerId.toString()))
        );
        var booking = bookingRepository.findById(bookingId).orElseThrow(() ->
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
                return bookingRepository.save(booking);
            } else {
                return booking;
            }
        }
        return booking;
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
        return bookingRepository.findById(bookingId).get();
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
        if (!userRepository.existsById(userId)) {
            throw new EntityRuntimeErrorException(thisService, USER_NOT_FOUND, ID.concat(userId.toString()));
        }
        return switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdIsOrderByStartDesc(userId);
            case WAITING -> bookingRepository.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, WAITING);
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
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, ID.concat(ownerId.toString()));
        }
        Set<Long> itemIds = itemRepository.getItemsByOwnerId(ownerId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toSet());
        return switch (state) {
            case ALL -> bookingRepository.findAllByItemIdInOrderByStartDesc(itemIds);
            case WAITING -> bookingRepository.findAllByItemIdInAndStatusIsOrderByStartDesc(itemIds, WAITING);
        };
    }

}
