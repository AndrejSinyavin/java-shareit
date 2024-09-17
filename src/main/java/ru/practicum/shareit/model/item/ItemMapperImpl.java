package ru.practicum.shareit.model.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDtoUpdate;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemMapperImpl implements ItemMapper {
    static Long ITEM_ID_EMPTY = 0L;

    @Override
    public Item toItem(ItemDtoCreate itemDto) {
        return new Item(
                ITEM_ID_EMPTY,
                itemDto.name(),
                itemDto.description(),
                itemDto.available(),
                null,
                null);
    }

    @Override
    public Item toItem(ItemDtoUpdate itemDto) {
        return new Item(
                ITEM_ID_EMPTY,
                itemDto.name(),
                itemDto.description(),
                itemDto.available(),
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
