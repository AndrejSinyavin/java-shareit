package ru.practicum.shareit.model.item;

public interface ItemMapperBase {

    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);
}
