package ru.practicum.shareit.model.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.entity.Item;
import ru.practicum.shareit.model.item.mapper.ItemMapperBase;
import ru.practicum.shareit.model.item.repository.ItemRepository;
import ru.practicum.shareit.model.user.repository.UserRepository;
import ru.practicum.shareit.validate.EntityValidate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImp implements ItemService {
    ItemRepository items;
    UserRepository users;
    ItemMapperBase mapper;
    EntityValidate validator;

    /**
     * @param itemDto
     * @param ownerId
     * @return
     */
    @Override
    public ItemDto add(ItemDto itemDto, Long ownerId) {
        var owner = users.get(ownerId);
        var data = mapper.toItem(itemDto);
        data.setOwner(owner);
        data = (Item) validator.validate(data);
        return mapper.toItemDto(items.add(data));
    }

    /**
     * * @param itemDto
     * @param ownerId
     * @return
     */
    @Override
    public ItemDto update(ItemDto itemDto, Long ownerId) {
        return null;
    }
}
