package ru.practicum.shareit.model.item.service;

import ru.practicum.shareit.model.item.dto.ItemDto;

public interface ItemService {

    public ItemDto add(ItemDto itemDto, Long ownerId);

    ItemDto update(ItemDto itemDto, Long ownerId);
}
