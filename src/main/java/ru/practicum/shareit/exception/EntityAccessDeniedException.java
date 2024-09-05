package ru.practicum.shareit.exception;

public class EntityAccessDeniedException extends AppException {

    public EntityAccessDeniedException(String source, String error, String message) {
        super(source, error, message);
    }
}
