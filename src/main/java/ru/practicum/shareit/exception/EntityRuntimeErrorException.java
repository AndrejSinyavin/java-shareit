package ru.practicum.shareit.exception;

public class EntityRuntimeErrorException extends EntityValidateException {
  public EntityRuntimeErrorException(String source, String error) {
    super(source, error);
  }

  public EntityRuntimeErrorException(String source, String error, String message) {
    super(source, error, message);
  }

}
