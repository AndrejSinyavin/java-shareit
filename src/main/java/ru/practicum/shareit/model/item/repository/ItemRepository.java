package ru.practicum.shareit.model.item.repository;

import ru.practicum.shareit.model.item.entity.Item;

import java.util.Collection;

public interface ItemRepository {

    Item add(Item item);

    Item update(Item data, Long id);

    Item get(Long id);

    Collection<Item> getAllByOwner(Long owner);

    Collection<Item> search(String searchString);
}
