package ru.practicum.shareit.exception;

public class EntityAlreadyExistsException extends AppException {

    public EntityAlreadyExistsException(String source, String error, String message) {
        super(source, error, message);
    }

}
