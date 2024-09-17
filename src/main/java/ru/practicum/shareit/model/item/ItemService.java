package ru.practicum.shareit.model.item;

import java.util.Collection;

/**
 * Интерфейс сервисов для работы с 'вещами'
 */
public interface ItemService {

    Item add(Item item, Long ownerId);

    Item update(Item item, Long itemId, Long ownerId);

    Item get(Long itemId);

    Collection<Item> getItemsByOwner(Long ownerId);

    Collection<Item> search(String search);

}
