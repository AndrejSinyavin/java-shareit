package ru.practicum.shareit.model.item;

import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDtoUpdate;

/**
 * Маппинг 'Item <-> DTO'
 */
public interface ItemMapper {

    Item toItem(ItemDtoCreate itemDto);

    Item toItem(ItemDtoUpdate itemDto);

    ItemDto toItemDto(Item item);
}
