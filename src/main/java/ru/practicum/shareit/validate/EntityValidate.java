package ru.practicum.shareit.validate;

/**
 * Интерфейс для подключения сервисов валидации данных в приложении
 */
public interface EntityValidate {

    /**
     * Метод выполняет валидацию всех полей сущности, имеющих аннотации валидации.
     *
     * @param entity сущность, объявленная с использованием интерфейса {@link Validated}
     * @return этот же объект, если валидация успешно выполнена
     */
    Object validate(Validated entity);

}