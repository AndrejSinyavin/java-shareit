package ru.practicum.shareit.model.item.service;

import ru.practicum.shareit.model.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto add(ItemDto itemDto, Long ownerId);

    ItemDto update(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDto get(Long itemId);

    Collection<ItemDto> getAllByOwner(Long ownerId);

    Collection<ItemDto> search(String searchString);

}
