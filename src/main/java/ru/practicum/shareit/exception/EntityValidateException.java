package ru.practicum.shareit.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class EntityValidateException extends AppException {
    private final Map<String, String> errors;

    public EntityValidateException(String source, String error, String message, Map<String, String> errors) {
        super(source, message, error);
        this.errors = errors;
    }

}
