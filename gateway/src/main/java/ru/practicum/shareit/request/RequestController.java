package ru.practicum.shareit.request;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.exception.EntityValidateException;
import ru.practicum.shareit.request.dto.ItemRequestDtoCreate;
import ru.practicum.shareit.validation.CustomEntityValidator;

import java.util.Optional;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestController {
    static String RESPONSE = "Response: {}";
    static String USER_UNDEFINED = "Не указан ID пользователя, создавшего запрос на сервер";
    static String ABSENT_HEADER = "Отсутствует заголовок ";
    static final String HEADER_SHARER = "X-Sharer-User-Id";
    static final String REQUEST_ID = "request-id";
    String thisService = this.getClass().getSimpleName();
    CustomEntityValidator entityValidator;
    RequestClient requestClient;

    @PostMapping
    public Object createItemRequest(@RequestBody @Valid ItemRequestDtoCreate requestBody,
                             @RequestHeader(value = HEADER_SHARER, required = false)
                             @Positive(message = "ID 'пользователя' должен быть положительным значением")
                             long ownerId) {
        checkSharerHeader(ownerId);
        entityValidator.validate(requestBody);
        var response = requestClient.add(ownerId, requestBody);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping("/{request-id}")
    public Object getItemRequest(@PathVariable(value = REQUEST_ID)
                                 long requestId,
                                 @RequestHeader(value = HEADER_SHARER, required = false)
                                 @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                 long ownerId) {
        checkSharerHeader(ownerId);
        var response = requestClient.get(ownerId, requestId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnersRequests(
                                            @RequestHeader(value = HEADER_SHARER, required = false)
                                            @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                            long ownerId) {
        var response = requestClient.getAllByOwner(ownerId);
        log.info(RESPONSE, response);
        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherUsersRequests(
                                            @RequestHeader(value = HEADER_SHARER, required = false)
                                            @Positive(message = "ID 'пользователя' должен быть положительным значением")
                                            long ownerId) {
        var response = requestClient.getAllOtherUsers(ownerId);
        log.info(RESPONSE, response);
        return response;
    }

    private void checkSharerHeader(Long ownerId) {
        Optional.ofNullable(ownerId).orElseThrow(
                () -> new EntityValidateException(
                        thisService, USER_UNDEFINED, ABSENT_HEADER.concat(HEADER_SHARER)
                )
        );
    }

}
