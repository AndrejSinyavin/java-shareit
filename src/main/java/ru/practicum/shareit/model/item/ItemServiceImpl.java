package ru.practicum.shareit.model.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.UserRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Реализация интерфейса {@link ItemService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {
    static String EMPTY_STRING = "";
    static String OWNER_ID = "ID ";
    static String ITEM_ID = OWNER_ID;
    static String OWNER_NOT_FOUND = "'Владелец' не найден в репозитории";
    static String ITEM_NOT_FOUND = "'Предмет' не найден в репозитории: ";
    static String ENTITY_UPDATE_ERROR = "Ошибка при обновлении сущности ";
    static String ACCESS_DENIED = "Пользователь не является владельцем 'предмета' ID: ";
    static Long ITEM_ID_EMPTY = 0L;
    String thisService = this.getClass().getSimpleName();
    ItemRepository items;
    UserRepository users;
    ItemMapperBase mapper;

    /**
     * Получение 'предмета' по его идентификатору
     *
     * @param itemId идентификатор 'предмета'
     * @return {@link ItemDto} со всеми полями
     */
    @Override
    public ItemDto get(Long itemId) {
        var item = items.findById(itemId)
                .orElseThrow(() ->
                new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
        return mapper.toItemDto(item);
    }

    /**
     * Добавление 'предмета'
     *
     * @param itemDto {@link ItemDto} с необходимыми установленными полями
     * @param ownerId идентификатор владельца
     * @return {@link ItemDto} со всеми полями и установленным ID
     */
    @Override
    public ItemDto add(ItemDto itemDto, Long ownerId) {
        var owner = users.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        Item data = mapper.toItem(itemDto);
        data.setId(ITEM_ID_EMPTY);
        data.setOwner(owner);
        try {
            return mapper.toItemDto(items.save(data));
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(
                    thisService,
                    EMPTY_STRING,
                    e.getMostSpecificCause().getLocalizedMessage()
            );
        }
    }

    /**
     * Обновление существующего 'предмета'
     *
     * @param itemDto {@link ItemDto} с необходимыми установленными полями
     * @param itemId идентификатор 'предмета'
     * @param ownerId идентификатор владельца
     * @return {@link ItemDto} с уже обновленными полями
     */
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        if (!users.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()));
        }
        var item = items.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
        if (!ownerId.equals(item.getOwner().getId())) {
            throw new EntityAccessDeniedException(
                    thisService, ENTITY_UPDATE_ERROR, ACCESS_DENIED.concat(itemId.toString())
            );
        }
        var data = mapper.toItem(itemDto);
        Optional.ofNullable(data.getName()).ifPresent(item::setName);
        Optional.ofNullable(data.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(data.getAvailable()).ifPresent(item::setAvailable);
            try {
                return mapper.toItemDto(items.save(item));
            } catch (DataIntegrityViolationException e) {
                throw new EntityAlreadyExistsException(
                        thisService,
                        EMPTY_STRING,
                        e.getMostSpecificCause().getLocalizedMessage()
                );
            }
        }

    /**
     * Получение списка всех предметов владельца
     *
     * @param ownerId идентификатор владельца
     * @return список {@link ItemDto}
     */
    @Override
    public Collection<ItemDto> getItemsByOwner(Long ownerId) {
        var owner = users.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        return items.getAllByOwnerOrderById(owner).stream().map(mapper::toItemDto).toList();
    }

    /**
     * Поиск всех предметов, имеющих в имени или описании заданный 'текст'.
     * В результат поиска попадают только доступные для аренды предметы.
     *
     * @param searchString искомый 'текст'
     * @return список из {@link ItemDto}
     */
    @Override
    public Collection<ItemDto> search(String searchString) {
        return items.findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(searchString, searchString)
                .stream().map(mapper::toItemDto).toList();
    }

}
