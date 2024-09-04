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
    static String SERVICE_FAILURE = "Сервис не смог проверить запрос ";
    static String INCORRECT_FIELDS = "Некорректные поля в запросе";

    @Override
    public Object validate(Object data) {
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
        return data;
    }

}
