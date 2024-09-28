package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityValidateException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Компонент, реализующий интерфейс {@link CustomEntityValidator}.
 * Реализация выполняет валидацию сущностей в приложении с помощью
 * API Jakarta Bean Validation {@linkplain jakarta.validation}.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityValidator implements CustomEntityValidator {
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    static String VALIDATION_ERROR = "Ошибка валидации";
    static String SERVICE_FAILURE = "Сервис не смог проверить запрос ";
    static String INCORRECT_FIELDS = "Некорректные поля в запросе";

    /**
     * Метод проверяет поля принимаемого объекта, помеченные аннотациями валидации.
     *
     * @param data сущность, объявленная с использованием интерфейса {@link ValidatedEntity}
     */
    @Override
    public void validate(ValidatedEntity data) {
        Set<ConstraintViolation<Object>> violations;
        Map<String, String> errors = new LinkedHashMap<>();
        try {
            violations = validator.validate(data);
        } catch (Exception e) {
            throw new EntityValidateException(
                    this.getClass().getSimpleName(), VALIDATION_ERROR, SERVICE_FAILURE, errors);
        }
        if (!violations.isEmpty()) {
            errors.put(data.getClass().getSimpleName(), VALIDATION_ERROR);
            for (var violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new EntityValidateException(
                    this.getClass().getSimpleName(), VALIDATION_ERROR, INCORRECT_FIELDS, errors);
        }
    }

}
