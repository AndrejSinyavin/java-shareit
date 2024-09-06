package ru.practicum.shareit.exception;

public class EntityNotFoundException extends AppException {

  public EntityNotFoundException(String source, String error, String message) {
    super(source, error, message);
  }

}
