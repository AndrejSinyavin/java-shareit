package ru.practicum.shareit.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Централизованный обработчик исключений приложения для REST-full API.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice
public class AppExceptionHandlers {
    static String SEPARATOR = ". ";
    static String BAD_REQUEST = "'400 Bad Request' ";
    static String CONFLICT = "'409 Conflict' ";
    static String NOT_FOUND = "'404 Not Found' ";
    static String FORBIDDEN = "'403 Forbidden' ";
    static String INTERNAL_SERVER_ERROR = "'500 Internal Server Error' ";
    static String NOT_READABLE_BODY = "Тело запроса некорректное или отсутствует.";
    static String INCORRECT_REQUEST = "Некорректный запрос.";
    static String ENTITY_NOT_FOUND = "Не найден объект, необходимый для выполнения запроса.";
    static String REQUEST_COULD_NOT_BE_PROCESSED = "Невозможно выполнить запрос.";
    static String SERVER_ERROR = "Сервер не смог обработать запрос.";
    static String SERVER_FAILURE = "Сбой в работе сервера.";
    static String LOG_RESPONSE_THREE = "Ответ <= {} {} \n{}";
    static String LOG_RESPONSE_FOUR = "Ответ <= {} {} {} \n{}";
    static String LOG_RESPONSE_FIVE = "Ответ <= {} {} {} {} \n{}";

    /**
     * Обработчик исключений для ответов BAD_REQUEST.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestResponse(final EntityValidateException e) {
        return warnBadRequest(e, INCORRECT_REQUEST);
    }

    private ErrorResponse warnBadRequest(EntityValidateException e, String incorrectRequest) {
        log.warn(LOG_RESPONSE_THREE, BAD_REQUEST, e.getErrors().toString(), e.getStackTrace());
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(BAD_REQUEST, incorrectRequest);
        errors.putAll(e.getErrors());
        return new ErrorResponse(errors);
    }

    /**
     * Обработчик исключений для ответов BAD_REQUEST при невозможности обработки данных в сервисном слое
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityRuntimeErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleErrorInProcessingOfDataResponse(final EntityRuntimeErrorException e) {
        return warnBadRequest(e, REQUEST_COULD_NOT_BE_PROCESSED);
    }

    /**
     * Обработчик исключений для ответов CONFLICT.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictRequestResponse(final AppException e) {
        log.warn(LOG_RESPONSE_FOUR, CONFLICT, e.getMessage(), e.getError(), e.getStackTrace());
        return new ErrorResponse(CONFLICT, e.getMessage().concat(SEPARATOR).concat(e.getError()));
    }

    /**
     * Обработчик исключений для ответов FORBIDDEN.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityAccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedResponse(final AppException e) {
        log.warn(LOG_RESPONSE_FOUR, FORBIDDEN, e.getMessage(), e.getError(), e.getStackTrace());
        return new ErrorResponse(FORBIDDEN, e.getMessage().concat(SEPARATOR).concat(e.getError()));
    }

    /**
     * Обработчик исключений для ответов BAD_REQUEST для запросов
     * с отсутствующим или несоответствующим форматом тела или заголовков.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableExceptionResponse(final HttpMessageNotReadableException e) {
        log.warn(LOG_RESPONSE_THREE, BAD_REQUEST, NOT_READABLE_BODY, e.getStackTrace());
        return new ErrorResponse(BAD_REQUEST, NOT_READABLE_BODY);
    }

    /**
     * Обработчик исключений для ответов NOT_FOUND.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundErrorResponse(final AppException e) {
        log.warn(LOG_RESPONSE_FIVE, NOT_FOUND, ENTITY_NOT_FOUND, e.getError(), e.getMessage(), e.getStackTrace());
        return new ErrorResponse(
                NOT_FOUND.concat(ENTITY_NOT_FOUND),
                e.getError().concat(SEPARATOR).concat(e.getMessage())
        );
    }

    /**
     * Обработчик исключений для ответов INTERNAL_SERVER_ERROR
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({InternalServiceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleServerInternalErrorResponse(final AppException e) {
        log.error(LOG_RESPONSE_FIVE, INTERNAL_SERVER_ERROR.concat(SERVER_ERROR),
                e.getSource(), e.getError(), e.getMessage(), e.getStackTrace());
        return new ErrorResponse(
                INTERNAL_SERVER_ERROR,
                SERVER_ERROR.concat(SEPARATOR).concat(e.getLocalizedMessage())
        );
    }

    /**
     * Обработчик исключений - заглушка, для обработки прочих непредусмотренных исключений.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerFailureResponse(final Throwable e) {
        log.error(INTERNAL_SERVER_ERROR.concat(SERVER_FAILURE).concat(Arrays.toString(e.getStackTrace())));
        return new ErrorResponse(INTERNAL_SERVER_ERROR.concat(SERVER_FAILURE), e.getMessage());
    }

}
