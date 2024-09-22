package ru.practicum.shareit.model.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    String thisService = this.getClass().getSimpleName();
    ItemRepository items;
    UserRepository users;

    /**
     * Получение 'предмета' по его идентификатору
     *
     * @param itemId идентификатор 'предмета'
     * @return {@link Item} со всеми полями
     */
    @Override
    public Item get(Long itemId) {
        return items.findById(itemId)
                .orElseThrow(() ->
                new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
    }

    /**
     * Добавление 'предмета'
     *
     * @param item {@link Item} с необходимыми установленными полями
     * @param ownerId идентификатор 'владельца'
     * @return {@link Item} с установленным ID
     */
    @Override
    public Item add(Item item, Long ownerId) {
        var owner = users.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        item.setOwner(owner);
        return items.save(item);
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
        if (!users.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()));
        }
        var targetItem = items.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(thisService, ITEM_NOT_FOUND, ITEM_ID.concat(itemId.toString())));
        if (!ownerId.equals(targetItem.getOwner().getId())) {
            throw new EntityAccessDeniedException(
                    thisService, ENTITY_UPDATE_ERROR, ACCESS_DENIED.concat(itemId.toString())
            );
        }
        Optional.ofNullable(data.getName()).ifPresent(targetItem::setName);
        Optional.ofNullable(data.getDescription()).ifPresent(targetItem::setDescription);
        Optional.ofNullable(data.getAvailable()).ifPresent(targetItem::setAvailable);
            return items.save(targetItem);
        }

    /**
     * Получение списка всех 'предметов' владельца
     *
     * @param ownerId идентификатор владельца
     * @return список {@link Item}
     */
    @Override
    public Collection<ItemDto> getItemsByOwner(Long ownerId) {
        var owner = users.findById(ownerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                thisService, OWNER_NOT_FOUND, OWNER_ID.concat(ownerId.toString()))
                );
        return items.getAllByOwnerOrderById(owner);
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
            return items.searchSubstring(search);
        }
    }

}
