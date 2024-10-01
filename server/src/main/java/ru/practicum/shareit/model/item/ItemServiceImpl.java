package ru.practicum.shareit.model.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.EntityRuntimeErrorException;
import ru.practicum.shareit.model.booking.BookingRepository;
import ru.practicum.shareit.model.booking.BookingStatus;
import ru.practicum.shareit.model.item.dto.CommentDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoBooking;
import ru.practicum.shareit.model.user.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

/**
 * Реализация интерфейса {@link ItemService} для работы с 'вещами'
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {
    static String OWNER_ID = "ID ";
    static String ITEM_ID = OWNER_ID;
    static String OWNER_NOT_FOUND = "'Владелец' не найден в репозитории";
    static String ITEM_NOT_FOUND = "'Предмет' не найден в репозитории: ";
    static String ENTITY_UPDATE_ERROR = "Ошибка при обновлении сущности ";
    static String ACCESS_DENIED = "Пользователь не является владельцем 'предмета' ID: ";
    static String USER_NOT_CREATE_BOOKING = "Пользователь не делал запроса на аренду предмета ";
    static String USER_NOT_USE_ITEM = "Пользователь еще не начал пользоваться предметом ";
    static String USER_NOT_FINISHED_USE_ITEM = "Пользователь еще не закончил пользоваться предметом ";
    static String OWNER_NOT_APPROVE_BOOKING = "Владелец не разрешал пользователю пользоваться предметом ";
    String thisService = this.getClass().getSimpleName();
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemMapper itemMapper;

    /**
     * Получение 'предмета' по его идентификатору
     *
     * @param itemId идентификатор 'предмета'
     * @return {@link Item} со всеми полями
     */
    @Override
    public ItemDtoBooking get(Long itemId) {
        var item = itemRepository.findById(itemId)
                .orElseThrow(() ->
                        new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
        var allComments = commentRepository.findByItem_IdOrderByAuthor_IdAsc(itemId)
                .stream()
                .map(itemMapper::toCommentDto)
                .toList();
        return new ItemDtoBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                allComments
        );
    }

    /**
     * Добавление 'предмета'
     *
     * @param item    {@link Item} с необходимыми установленными полями
     * @param ownerId идентификатор 'владельца'
     * @return {@link Item} с установленным ID
     */
    @Override
    public Item add(Item item, Long ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    /**
     * Обновление существующего 'предмета'
     *
     * @param data {@link Item} с необходимыми установленными полями
     * @param itemId идентификатор 'предмета'
     * @param ownerId идентификатор 'владельца'
     * @return {@link Item} с обновленными полями
     */
    @Override
    public Item update(Item data, Long itemId, Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()));
        }
        var targetItem = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
        if (!ownerId.equals(targetItem.getOwner().getId())) {
            throw new EntityAccessDeniedException(
                    thisService, ENTITY_UPDATE_ERROR, ACCESS_DENIED.concat(itemId.toString())
            );
        }
        Optional.ofNullable(data.getName()).ifPresent(targetItem::setName);
        Optional.ofNullable(data.getDescription()).ifPresent(targetItem::setDescription);
        Optional.ofNullable(data.getAvailable()).ifPresent(targetItem::setAvailable);
            return itemRepository.save(targetItem);
        }

    /**
     * Получение списка всех 'предметов' владельца
     *
     * @param ownerId идентификатор владельца
     * @return список {@link Item}
     */
    @Override
    public Collection<ItemDtoBooking> getItemsByOwner(Long ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        var allOwnersItems = itemRepository.getAllByOwnerOrderById(owner);
        var allOwnersItemDtoBookings = new HashMap<Long, ItemDtoBooking>();
        var itemIds = allOwnersItems
                .stream()
                .peek(itemDto -> allOwnersItemDtoBookings.put(
                        itemDto.id(),
                        new ItemDtoBooking(
                                itemDto.id(),
                                itemDto.name(),
                                itemDto.description(),
                                itemDto.available(),
                                null,
                                null,
                                null
                        )
                ))
                .map(ItemDto::id)
                .toList();
        var allBooking = bookingRepository.getAllByItemIdIn(itemIds);
        var now = LocalDateTime.now();
        allBooking.forEach(
                booking -> {
                    var item = allOwnersItemDtoBookings.get(booking.getId());
                    var bookingStart =  LocalDateTime.ofInstant(booking.getStart(), UTC);
                    if (bookingStart.isBefore(now)) {
                        var previous = item.getLastBooking();
                        if (previous == null) {
                            previous = bookingStart;
                        } else {
                            if (previous.isBefore(bookingStart)) {
                                previous = bookingStart;
                            }
                        }
                        item.setLastBooking(previous);
                    } else {
                        var next = item.getNextBooking();
                        if (next == null) {
                            next = bookingStart;
                        } else {
                            if (next.isAfter(bookingStart)) {
                                next = bookingStart;
                            }
                        }
                        item.setNextBooking(next);
                    }
                }

        );
        return allOwnersItemDtoBookings.values();
    }

    /**
     * Поиск всех предметов, имеющих в имени или описании заданный 'текст'.
     * В результат поиска попадают только доступные для аренды предметы.
     *
     * @param search искомый 'текст'
     * @return список из {@link Item}
     */
    @Override
    public Collection<Item> search(String search) {
        if (search == null || search.isBlank()) {
            return List.of();
        } else {
            return itemRepository.searchSubstring(search);
        }
    }

    /**
     * Добавление комментария к предмету, который взят в аренду, или уже был в аренде
     *
     * @param userId идентификатор автора предполагаемого комментария
     * @param itemId идентификатор предмета, которому написан комментарий
     * @param comment текст комментария
     * @return сформированный комментарий, записанный в базу
     */
    @Override
    public Comment addComment(Long userId, Long itemId, CommentDtoCreate comment) {
        var item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(
                        thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString()))
        );
        var user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        thisService, OWNER_NOT_FOUND, OWNER_ID.concat(userId.toString())
                )
        );
        var now = LocalDateTime.now().toInstant(UTC);
        var booking = bookingRepository.findByItemIsAndBookerIs(item, user).orElseThrow(
                        () -> new EntityNotFoundException(
                                thisService,
                                USER_NOT_CREATE_BOOKING,
                                ITEM_ID.concat(itemId.toString()))
        );
        if (!booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new EntityAccessDeniedException(
                    thisService,
                    OWNER_NOT_APPROVE_BOOKING,
                    ITEM_ID.concat(itemId.toString())
            );
        } else if (booking.getStart().isAfter(now)) {
                throw new EntityRuntimeErrorException(
                        thisService,
                        USER_NOT_USE_ITEM.concat(ITEM_ID.concat(itemId.toString()))
                );
        } else if (booking.getEnd().isAfter(now)) {
            throw new EntityRuntimeErrorException(
                    thisService, USER_NOT_FINISHED_USE_ITEM.concat(ITEM_ID).concat(itemId.toString())
            );
        }
        return commentRepository.save(new Comment(0L, item, user, comment.text(), Instant.now()));
    }

}
