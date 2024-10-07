package ru.practicum.shareit.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    static String BAD_REQUEST = "'400 Bad Request' ";
    static String INTERNAL_SERVER_ERROR = "'500 Internal Server Error' ";
    static String INCORRECT_REQUEST = "Неправильные или отсутствующие в запросе поля, заголовки, переменные пути.";
    static String SERVER_FAILURE = "Сбой в работе сервера.";
    static String VALIDATE_CONTROLLER = "Валидация запроса в контроллере.";
    static String INCORRECT_FIELDS = "Обнаружены некорректные параметры в запросе.";
    static String LOG_RESPONSE_THREE = "Ответ <= {} {} \n{}";

    /**
     * Обработчик исключений для ответов BAD_REQUEST при валидации входящих данных в контроллерах.
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({EntityValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestResponse(final EntityValidateException e) {
        log.warn(LOG_RESPONSE_THREE, BAD_REQUEST, e.getErrors().toString(), e.getStackTrace());
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(BAD_REQUEST, INCORRECT_REQUEST);
        errors.putAll(e.getErrors());
        return new ErrorResponse(errors);
    }

    /**
     * Обработчик исключений для ответов BAD_REQUEST при валидации аннотациями
     *
     * @param e перехваченное исключение
     * @return стандартный API-ответ об ошибке ErrorResponse с описанием ошибки и вероятных причинах
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAnnotationValidateErrorResponse(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put(BAD_REQUEST, INCORRECT_REQUEST);
        errors.put(VALIDATE_CONTROLLER, INCORRECT_FIELDS);
        e.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    errors.put(fieldName, error.getDefaultMessage());
                });
        log.warn(LOG_RESPONSE_THREE, BAD_REQUEST, errors, e.getStackTrace());
        return new ErrorResponse(errors);
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
