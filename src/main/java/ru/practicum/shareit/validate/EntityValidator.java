package ru.practicum.shareit.validate;

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

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityValidator implements EntityValidate {
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    static String VALIDATION_ERROR = "Ошибка валидации";

    @Override
    public Object validate(Object entity) {
        Set<ConstraintViolation<Object>> violations;
        Map<String, String> errors = new LinkedHashMap<>();
        try {
            violations = validator.validate(entity);
        } catch (Exception e) {
            throw new EntityValidateException(
                    this.getClass().getSimpleName(), VALIDATION_ERROR, "Сервис не смог проверить запрос ", errors);
        }
        if (!violations.isEmpty()) {
            errors.put(entity.getClass().getSimpleName(), VALIDATION_ERROR);
            for (var violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new EntityValidateException(
                    this.getClass().getSimpleName(), VALIDATION_ERROR, "Некорректные поля в запросе", errors);
        }
        return entity;
    }

}
