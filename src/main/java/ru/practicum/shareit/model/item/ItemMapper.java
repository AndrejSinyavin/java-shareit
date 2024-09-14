package ru.practicum.shareit.model.item;

import org.springframework.stereotype.Component;

@Component
public class ItemMapper implements ItemMapperBase {

    @Override
    public Item toItem(ItemDto itemDto) {
        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null);
    }

    @Override
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }

}
