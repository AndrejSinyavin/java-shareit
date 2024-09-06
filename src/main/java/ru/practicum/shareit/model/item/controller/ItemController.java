package ru.practicum.shareit.model.item.controller;

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
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.service.ItemService;

import java.util.Collection;
import java.util.Map;
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
    static String EMPTY_STRING = "";
    static String RESPONSE_OK = "Ответ: '200 OK' {} ";
    static String RESPONSE_CREATED = "Ответ: '201 Created' {} ";
    static String OWNER_ID = "владелец с ID[{}]";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String ITEM_ID = "item-id";
    static final String SEARCH_STRING = "text";
    String thisService = this.getClass().getSimpleName();
    ItemService items;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto add(@RequestBody() ItemDto itemDto,
                       @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        log.info("Запрос POST: создать предмет {} владелец ID[{}]", itemDto, ownerId);
        checkSharerHeader(ownerId);
        var response = items.add(itemDto, ownerId);
        log.info(RESPONSE_CREATED.concat(OWNER_ID), response, ownerId);
        return response;
    }

    @PatchMapping("/{item-id}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable(value = ITEM_ID) Long itemId,
                          @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId
                          ) {
        log.info("Запрос PATCH: обновить предмет ID[{}] значениями {} владелец ID[{}]", itemId, itemDto, ownerId);
        checkSharerHeader(ownerId);
        var response = items.update(itemDto, itemId, ownerId);
        log.info(RESPONSE_OK.concat(OWNER_ID), response, ownerId);
        return response;
    }

    @GetMapping("/{item-id}")
    public ItemDto get(@PathVariable(value = ITEM_ID) Long itemId) {
        log.info("Запрос GET: показать предмет с ID[{}] любому пользователю", itemId);
        var response = items.get(itemId);
        log.info(RESPONSE_OK, response);
        return response;
    }

    @GetMapping
    public Collection<ItemDto> list(@RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        log.info("Запрос GET: показать владельцу с ID[{}] список его предметов ", ownerId);
        checkSharerHeader(ownerId);
        var response = items.getAllByOwner(ownerId);
        log.info(RESPONSE_OK, response);
        return response;
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(value = SEARCH_STRING) String searchString) {
        log.info("Запрос GET: найти предмет с текстом '{}' в названии или описании", searchString);
        var response = items.search(searchString);
        log.info(RESPONSE_OK, response);
        return response;
    }

    private void checkSharerHeader(Long ownerId) {
        Optional.ofNullable(ownerId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, EMPTY_STRING, EMPTY_STRING,
                        Map.of("Отсутствует заголовок '".concat(HEADER_SHARER).concat("'"),
                                "Не указан владелец вещи"
                        )
                )
        );
    }

}
