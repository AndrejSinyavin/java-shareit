package ru.practicum.shareit.model.item;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.model.item.dto.CommentDto;
import ru.practicum.shareit.model.item.dto.CommentDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoBooking;
import ru.practicum.shareit.model.item.dto.ItemDtoCreate;
import ru.practicum.shareit.model.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Collection;
import java.util.Optional;

/**
 * Контроллер обработки REST-запросов для работы с 'вещами'
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    static String RESPONSE_OK = "Ответ: '200 OK' {} ";
    static String RESPONSE_CREATED = "Ответ: '201 Created' {} ";
    static String OWNER_ID = "владелец с ID[{}]";
    static String OWNER_UNDEFINED = "Не указан владелец вещи";
    static String ABSENT_HEADER = "Отсутствует заголовок ";
    static String UPDATE_REQUEST = "Запрос PATCH: обновить предмет ID[{}] значениями {} владелец ID[{}]";
    static String POST_REQUEST = "Запрос POST: создать предмет {} владелец ID[{}]";
    static String GET_ITEM_REQUEST = "Запрос GET: показать предмет с ID[{}] любому пользователю";
    static String GET_OWNER_LIST_REQUEST = "Запрос GET: показать владельцу с ID[{}] список его предметов";
    static String FIND_ITEM_REQUEST = "Запрос GET: найти предмет с текстом '{}' в названии или описании";
    static String ADD_COMMENT_REQUEST = "Запрос POST: пользователь ID[{}] создает для предмета ID[{}] комментарий {}";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String ITEM_ID = "item-id";
    static final String SEARCH_STRING = "text";
    String thisService = this.getClass().getSimpleName();
    ItemMapper itemMapper;
    CustomEntityValidator entityValidator;
    ItemService ititemServicems;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto add(@RequestBody() ItemDtoCreate item,
                       @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        log.info(POST_REQUEST, item, ownerId);
        checkSharerHeader(ownerId);
        entityValidator.validate(item);
        var response = itemMapper.toItemDto((ititemServicems.add(itemMapper.toItem(item), ownerId)));
        log.info(RESPONSE_CREATED.concat(OWNER_ID), response.toString(), ownerId);
        return response;
    }

    @PatchMapping("/{item-id}")
    public ItemDto update(@RequestBody ItemDtoUpdate item,
                          @Positive(message = "ID не может быть отрицательным значением")
                          @PathVariable(value = ITEM_ID) Long itemId,
                          @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId
                          ) {
        log.info(UPDATE_REQUEST, itemId, item, ownerId);
        checkSharerHeader(ownerId);
        entityValidator.validate(item);
        var response = itemMapper.toItemDto(ititemServicems.update(itemMapper.toItem(item), itemId, ownerId));
        log.info(RESPONSE_OK.concat(OWNER_ID), response.toString(), ownerId);
        return response;
    }

    @GetMapping("/{item-id}")
    public ItemDtoBooking get(@PathVariable(value = ITEM_ID)
                           @Positive(message = "ID не может быть отрицательным значением")
                           Long itemId) {
        log.info(GET_ITEM_REQUEST, itemId);
        var response = ititemServicems.get(itemId);
        log.info(RESPONSE_OK, response.toString());
        return response;
    }

    @GetMapping
    public Collection<ItemDtoBooking> list(@RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        log.info(GET_OWNER_LIST_REQUEST, ownerId);
        checkSharerHeader(ownerId);
        var response = ititemServicems.getItemsByOwner(ownerId);
        log.info(RESPONSE_OK, response);
        return response;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(value = SEARCH_STRING) String searchString) {
        log.info(FIND_ITEM_REQUEST, searchString);
        var response = ititemServicems.search(searchString)
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
        log.info(RESPONSE_OK, response);
        return response;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{item-id}/comment")
    private CommentDto addComment(
            @RequestBody CommentDtoCreate comment,
            @PathVariable(value = ITEM_ID) Long itemId,
            @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        log.info(ADD_COMMENT_REQUEST, ownerId, itemId, comment);
        checkSharerHeader(ownerId);
        var response = itemMapper.toCommentDto(ititemServicems.addComment(ownerId, itemId, comment));
        log.info(RESPONSE_CREATED, response);
        return response;
    }

    private void checkSharerHeader(Long ownerId) {
        Optional.ofNullable(ownerId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, OWNER_UNDEFINED, ABSENT_HEADER.concat(HEADER_SHARER)
                )
        );
    }

}
