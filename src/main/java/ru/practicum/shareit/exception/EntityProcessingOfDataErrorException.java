package ru.practicum.shareit.exception;

import java.util.Map;

public class EntityProcessingOfDataErrorException extends EntityValidateException {
  public EntityProcessingOfDataErrorException(String source, String error) {
    super(source, error);
  }

  public EntityProcessingOfDataErrorException(String source, String error, String message) {
    super(source, error, message);
  }

  public EntityProcessingOfDataErrorException(String source, String error, String message, Map<String, String> errors) {
    super(source, error, message, errors);
  }

}
