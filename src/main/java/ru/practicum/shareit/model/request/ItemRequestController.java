package ru.practicum.shareit.model.request;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.model.request.dto.ItemRequestDto;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.List;
import java.util.Optional;

/**
 * Контроллер обработки REST-запросов для работы с запросами на добавление вещей, отсутствующих в общем списке
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemRequestController {
    static String RESPONSE_OK = "Ответ: '200 OK' {} ";
    static String RESPONSE_CREATED = "Ответ: '201 Created' {} ";
    static String POST_REQUEST = "Запрос POST: создать запрос от 'пользователя' ID[{}] на добавление вещи [{}]";
    static String USER_UNDEFINED = "Не указан ID пользователя, создавшего запрос на сервер";
    static String ABSENT_HEADER = "Отсутствует заголовок ";
    static String GET_ONE_REQUEST =
            "Запрос GET: просмотреть все предложения по запросу на аренду предмета ID[{}]";
    static String GET_ALL_BY_OWNER_REQUESTS =
            "Запрос GET: посмотреть все предложения по всем своим запросам на аренду предметов для пользователя ID[{}]";
    static String GET_ALL_OTHER_USERS_REQUESTS =
            "Запрос GET: просмотреть все запросы всех пользователей";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String REQUEST_ID = "request-id";
    String thisService = this.getClass().getSimpleName();
    CustomEntityValidator dtoValidator;
    ItemRequestMapper itemRequestMapper;
    ItemRequestService itemRequestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody final ItemRequestDtoCreate requestBody,
                                            @RequestHeader(value = HEADER_SHARER, required = false)
                                            @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                            final Long ownerId) {
        log.info(POST_REQUEST, ownerId, requestBody.description());
        checkSharerHeader(ownerId);
        dtoValidator.validate(requestBody);
        var response = itemRequestMapper.toItemRequestDto(
                itemRequestService.add(itemRequestMapper.toItemRequest(requestBody), ownerId));
        log.info(RESPONSE_CREATED.concat(response.toString()));
        return response;
    }

    @GetMapping("/{request-id}")
    public ItemRequestDtoWithAnswer getItemRequest(@PathVariable(value = REQUEST_ID)
                                         final Long requestId,
                                         @RequestHeader(value = HEADER_SHARER, required = false)
                                         @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                         final Long ownerId) {
        log.info(GET_ONE_REQUEST, requestId);
        checkSharerHeader(ownerId);
        var response = itemRequestService.getItemRequest(requestId, ownerId);
        log.info(RESPONSE_OK.concat(response.toString()));
        return response;
    }

    @GetMapping
    public List<ItemRequestDtoWithAnswer> getAllOwnersRequests(
                                    @RequestHeader(value = HEADER_SHARER, required = false)
                                    @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                    final Long ownerId) {
        log.info(GET_ALL_BY_OWNER_REQUESTS, ownerId);
        var response = itemRequestService.getAllOwnersRequests(ownerId);
        log.info(RESPONSE_OK.concat((response.toString())));
        return response;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllOtherUsersRequests(
                                    @RequestHeader(value = HEADER_SHARER, required = false)
                                    @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                    final Long ownerId) {
        log.info(GET_ALL_OTHER_USERS_REQUESTS);
        var response = itemRequestService.getAllRequest(ownerId);
        log.info(RESPONSE_OK);
        return response;
    }

    private void checkSharerHeader(Long userId) {
        Optional.ofNullable(userId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, USER_UNDEFINED, ABSENT_HEADER.concat(HEADER_SHARER)
                )
        );
    }

}
