package ru.practicum.shareit.model.item.mapper;

import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.entity.Item;

public interface ItemMapperBase {
    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);
}
