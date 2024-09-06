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
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Collection;

/**
 * Реализация интерфейса {@link ItemService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImp implements ItemService {
    ItemRepository items;
    UserRepository users;
    ItemMapperBase mapper;
    CustomEntityValidator validator;

    /**
     * Добавление 'предмета'
     *
     * @param itemDto {@link ItemDto} с необходимыми установленными полями
     * @param ownerId идентификатор владельца
     * @return {@link ItemDto} со всеми полями и установленным ID
     */
    @Override
    public ItemDto add(ItemDto itemDto, Long ownerId) {
        users.get(ownerId);
        Item data = mapper.toItem(itemDto);
        data.setOwner(ownerId);
        Item item = items.add((Item) validator.validate(data));
        return mapper.toItemDto(item);
    }

    /**
     * Обновление существующего 'предмета'
     *
     * @param itemDto {@link ItemDto} с необходимыми установленными полями
     * @param itemId идентификатор 'предмета'
     * @param ownerId идентификатор владельца
     * @return {@link ItemDto} с уже обновленными полями
     */
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        users.get(ownerId);
        var data = mapper.toItem((ItemDto) validator.validate(itemDto));
        data.setOwner(ownerId);
        return mapper.toItemDto(items.update(data, itemId));
    }

    /**
     * Получение 'предмета' по его идентификатору
     *
     * @param itemId идентификатор 'предмета'
     * @return {@link ItemDto} со всеми полями
     */
    @Override
    public ItemDto get(Long itemId) {
        var item = items.get(itemId);
        return mapper.toItemDto(item);
    }

    /**
     * Получение списка всех предметов владельца
     *
     * @param ownerId идентификатор владельца
     * @return список {@link ItemDto}
     */
    @Override
    public Collection<ItemDto> getAllByOwner(Long ownerId) {
        users.get(ownerId);
        return items.getAllByOwner(ownerId)
                .stream()
                .map(mapper::toItemDto)
                .toList();
    }

    /**
     * Поиск всех предметов, имеющих в имени или описании заданный 'текст'.
     * В результат поиска попадают только доступные для аренды предметы.
     *
     * @param searchString искомый 'текст'
     * @return список из {@link ItemDto}
     */
    @Override
    public Collection<ItemDto> search(String searchString) {
        return items.search(searchString)
                .stream()
                .map(mapper::toItemDto)
                .toList();
    }

}
