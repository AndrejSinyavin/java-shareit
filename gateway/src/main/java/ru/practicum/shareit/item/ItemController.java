package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Optional;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {
    static String RESPONSE = "Response: {}";
    static String OWNER_UNDEFINED = "Не указан владелец вещи";
    static String ABSENT_HEADER = "Отсутствует заголовок ";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String ITEM_ID = "item-id";
    static final String SEARCH_STRING = "text";
    String thisService = this.getClass().getSimpleName();
    CustomEntityValidator entityValidator;
    ItemClient itemClient;

    @PostMapping
    public Object add(@RequestBody @Valid ItemDtoCreate item,
                       @RequestHeader(value = HEADER_SHARER, required = false) Long ownerId) {
        checkSharerHeader(ownerId);
        entityValidator.validate(item);
        var response = itemClient.add(item, ownerId);
        log.info(RESPONSE, response);
        return response;
    }

    @PatchMapping("/{item-id}")
    public Object update(@RequestBody ItemDtoUpdate item,
                          @Positive(message = "ID не может быть отрицательным значением")
                          @PathVariable(value = ITEM_ID) long itemId,
                          @RequestHeader(value = HEADER_SHARER, required = false) long ownerId
    ) {
        checkSharerHeader(ownerId);
        entityValidator.validate(item);
        var response = itemClient.update(item, ownerId, itemId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping("/{item-id}")
    public Object get(@PathVariable(value = ITEM_ID)
                      @Positive(message = "ID не может быть отрицательным значением")
                      long itemId) {
        var response = itemClient.get(itemId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> list(@RequestHeader(value = HEADER_SHARER, required = false)
                                       @Positive(message = "ID не может быть отрицательным значением")
                                       long ownerId) {
        checkSharerHeader(ownerId);
        var response = itemClient.list(ownerId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(value = SEARCH_STRING) String searchString) {
        var response = itemClient.search(searchString);
        log.info(RESPONSE, response);
        return response;
    }

    @PostMapping("/{item-id}/comment")
    private Object addComment(
            @RequestBody CommentDtoCreate comment,
            @Positive(message = "ID не может быть отрицательным значением")
            @PathVariable(value = ITEM_ID) long itemId,
            @Positive(message = "ID не может быть отрицательным значением")
            @RequestHeader(value = HEADER_SHARER, required = false) long ownerId) {
        checkSharerHeader(ownerId);
        var response = itemClient.addComment(comment, ownerId, itemId);
        log.info(RESPONSE, response);
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
