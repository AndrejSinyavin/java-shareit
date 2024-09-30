package ru.practicum.shareit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityValidateException extends AppException {
    Map<String, String> errors;
    static String EMPTY = "";

    public EntityValidateException(String source, String error, String message, Map<String, String> errors) {
        super(source, message, error);
        this.errors = errors;
    }

    public EntityValidateException(String source, String error, String message) {
        super(source, EMPTY, EMPTY);
        this.errors = Map.of(error, message);
    }

    public EntityValidateException(String source, String error) {
        super(source, EMPTY, EMPTY);
        this.errors = Map.of(error, EMPTY);
    }

}
