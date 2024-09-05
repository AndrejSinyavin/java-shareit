package ru.practicum.shareit.model.item.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityAccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.item.entity.Item;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Реализация интерфейса {@link ItemRepository} для хранения предметов в памяти
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class ItemRepositoryInMemoryImp implements ItemRepository {
    static final String ENTITY_UPDATE_ERROR = "Ошибка при обновлении сущности ";
    static final String ITEM_NOT_FOUND = "Не найден 'предмет' с указанным ID: ";
    static final String ACCESS_DENIED = "Пользователь не является владельцем 'предмета' ID: ";
    static final String EMPTY_STRING = "";
    final String thisService = this.getClass().getSimpleName();
    final Map<Long, Item> items = new HashMap<>();
    Long idCount = 0L;

    /**
     * Добавление 'предмета'
     *
     * @param data {@link Item} набор полей для создания
     * @return 'предмет' {@link Item} с созданным идентификатором
     */
    @Override
    public Item add(Item data) {
        var itemId = getNewId();
        var item = newItem(data, itemId);
        items.put(itemId, item);
        return newItem(item);
    }

    /**
     * Обновление существующего 'предмета'
     *
     * @param data {@link Item} набор необходимых полей для обновления
     * @param itemId идентификатор 'предмета'
     * @return обновленный 'предмета' {@link Item}
     */
    @Override
    public Item update(Item data, Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            log.warn(ENTITY_UPDATE_ERROR.concat(ITEM_NOT_FOUND).concat(itemId.toString()));
            throw new EntityNotFoundException(
                    thisService, ENTITY_UPDATE_ERROR, ITEM_NOT_FOUND.concat(itemId.toString()));
        } else if (!Objects.equals(data.getOwner(), item.getOwner())) {
            log.warn(ENTITY_UPDATE_ERROR.concat(ACCESS_DENIED).concat(itemId.toString()));
            throw new EntityAccessDeniedException(
                    thisService, ENTITY_UPDATE_ERROR, ACCESS_DENIED.concat(itemId.toString())
            );
        }
        Optional.ofNullable(data.getName()).ifPresent(item::setName);
        Optional.ofNullable(data.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(data.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(data.getOwner()).ifPresent(item::setOwner);
        Optional.ofNullable(data.getRequest()).ifPresent(item::setRequest);
        return newItem(item);
    }

    /**
     * Получение 'предмета'
     *
     * @param itemId идентификатор 'предмета'
     * @return 'предмет' {@link Item}
     */
    @Override
    public Item get(Long itemId) {
        var item = items.get(itemId);
        if (item == null) {
            log.warn(ITEM_NOT_FOUND.concat(itemId.toString()));
            throw new EntityNotFoundException(thisService, ITEM_NOT_FOUND, EMPTY_STRING);
        } else {
            return newItem(item);
        }
    }

    /**
     * Получение списка всех предметов владельца
     *
     * @param ownerId идентификатор владельца
     * @return список {@link Item}
     */
    @Override
    public Collection<Item> getAllByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .map(ItemRepositoryInMemoryImp::newItem)
                .sorted(Comparator.comparingLong(Item::getId))
                .toList();
    }

    /**
     * Поиск всех предметов, имеющих в имени или описании заданный 'текст'.
     * В результат поиска попадают только доступные для аренды предметы. Поиск не зависит от регистра символов.
     *
     * @param searchString искомый 'текст'
     * @return список {@link Item}
     */
    @Override
    public Collection<Item> search(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getAvailable() &
                        (item.getName().toLowerCase().contains(searchString.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(searchString.toLowerCase())
                        )
                )
                .sorted(Comparator.comparingLong(Item::getId))
                .toList();
    }

    private Long getNewId() {
        return ++idCount;
    }

    private static Item newItem(Item item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    private static Item newItem(Item item, Long id) {
        return new Item(
                id,
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

}
