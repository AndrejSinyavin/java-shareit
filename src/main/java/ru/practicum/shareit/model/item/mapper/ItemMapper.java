package ru.practicum.shareit.model.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.entity.Item;

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
