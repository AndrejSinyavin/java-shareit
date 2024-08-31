package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Централизованный обработчик исключений приложения для REST-full API.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice
public class AppExceptionHandlers {
    static String BAD_REQUEST = "'400 Bad Request'";
    static String CONFLICT = "'409 Conflict'";
    static String NOT_FOUND = "'404 Not Found' ";
    static String NOT_READABLE_BODY = "Тело запроса некорректное или отсутствует.";
    static String INCORRECT_FIELD = "Неправильные или отсутствующие поля в запросе.";
    static String ENTITY_NOT_FOUND = "Не найден объект, необходимый для выполнения запроса.";

    /**
     * Обработчик исключений для ответов BAD_REQUEST.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestResponse(final EntityValidateException e) {
        log.warn("Ответ <= {} {} \n{}", BAD_REQUEST, e.getErrors().values(), e.getStackTrace());
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(BAD_REQUEST, INCORRECT_FIELD);
        errors.putAll(e.getErrors());
        return new ErrorResponse(errors);
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
        log.warn("Ответ <= {} {} {} \n{}", CONFLICT, e.getMessage(), e.getError(), e.getStackTrace());
        return new ErrorResponse(CONFLICT, e.getMessage().concat(" ").concat(e.getError()));
    }

    /**
     * Обработчик исключений для ответов BAD_REQUEST для запросов с несоответствующим форматом тела или заголовков.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableExceptionResponse(final HttpMessageNotReadableException e) {
        log.warn("Ответ <= {} {} \n{}", BAD_REQUEST, NOT_READABLE_BODY, e.getStackTrace());
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
        log.warn("Ответ <= {} {} {} {} \n{}",
                NOT_FOUND, ENTITY_NOT_FOUND, e.getError(), e.getMessage(), e.getStackTrace());
        return new ErrorResponse(
                NOT_FOUND.concat(ENTITY_NOT_FOUND), e.getError().concat(". ").concat(e.getMessage())
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
        String message = "Сервер не смог обработать запрос. Сформирован ответ '500 Internal Server Error'";
        log.warn("Ответ <= {} {} {} {} {}", message, e.getSource(), e.getError(), e.getMessage(), e.getStackTrace());
        return new ErrorResponse(message, e.getMessage());
    }

    /**
     * Обработчик исключений для ответов BAD_REQUEST при валидации в контроллере
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAnnotationValidateErrorResponse(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(BAD_REQUEST, "В запросе отсутствуют обязательные поля, либо имеют неверный формат");
        errors.put("Валидация запроса в контроллере", "Обнаружены некорректные параметры в запросе");
        e.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        log.warn("Ответ <= {} {} {}", BAD_REQUEST, errors, e.getStackTrace());
        return new ErrorResponse(errors);
    }

    /**
     * Обработчик исключений для ответов NOT_FOUND при обработке параметров и/или переменных пути в запросах
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidMetodParameterErrorResponse(final ConstraintViolationException e) {
        String message = "Некорректный параметр или переменная пути в запросе.";
        log.warn("{} Сформирован ответ '404 Not Found.' {} {}", message, e.getLocalizedMessage(), e.getStackTrace());
        return new ErrorResponse(message, e.getLocalizedMessage());
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
        String message = "Сбой в работе сервера.";
        log.warn("{} Сформирован ответ '500 Internal Server Error.{}",
                message, e.getStackTrace());
        return new ErrorResponse(message, e.toString());
    }

}
