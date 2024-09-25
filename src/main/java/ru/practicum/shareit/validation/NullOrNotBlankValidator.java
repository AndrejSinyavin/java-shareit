package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Реализация {@link NullOrNotBlankValidator}
 */
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || !value.trim().isEmpty();
    }
}
