package ru.practicum.shareit.model.item;

import ru.practicum.shareit.model.item.dto.CommentDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDtoBooking;

import java.util.Collection;

/**
 * Интерфейс сервисов для работы с 'вещами'
 */
public interface ItemService {

    Item add(Item item, Long ownerId);

    Item update(Item item, Long itemId, Long ownerId);

    ItemDtoBooking get(Long itemId);

    Collection<ItemDtoBooking> getItemsByOwner(Long ownerId);

    Collection<Item> search(String search);

    Comment addComment(Long userId, Long itemId, CommentDtoCreate comment);
}
