package ru.practicum.shareit.validation;

/**
 * Интерфейс для подключения сервисов валидации данных в приложении
 */
public interface CustomEntityValidator {

    /**
     * Метод выполняет валидацию всех полей сущности, имеющих аннотации валидации.
     *
     * @param entity сущность, объявленная с использованием интерфейса {@link ValidatedEntity}
     */
    void validate(ValidatedEntity entity);

}