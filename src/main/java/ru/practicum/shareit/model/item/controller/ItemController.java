package ru.practicum.shareit.model.item.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.service.ItemService;

import java.util.Map;
import java.util.Optional;

/**
 * Контроллер обработки REST-запросов для работы с вещами
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    String thisService = this.getClass().getSimpleName();
    ItemService items;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto add(@RequestBody() ItemDto itemDto,
                       @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        log.info("Запрос => POST {} владелец ID[{}]", itemDto, ownerId);
        checkSharerHeader(ownerId);
        var response = items.add(itemDto, ownerId);
        log.info("Ответ <= '201 Created' {} владелец ID[{}]", response, ownerId);
        return response;
    }

    @PatchMapping
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId
                          ) {
        log.info("Запрос => PATCH {} владелец ID[{}]", itemDto, ownerId);
        checkSharerHeader(ownerId);
        var response = items.update(itemDto, ownerId);
        log.info("Ответ <= '200 OK' {} владелец ID[{}]", response, ownerId);
        return response;
    }

    private void checkSharerHeader(Long ownerId) {
        Optional.ofNullable(ownerId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, "", "",
                        Map.of("Отсутствует заголовок 'X-Sharer-User-Id'", "Не указан владелец вещи"))
        );
    }

}
