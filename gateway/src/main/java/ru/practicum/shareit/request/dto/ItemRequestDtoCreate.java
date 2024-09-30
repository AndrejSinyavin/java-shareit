package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO для создания DAO 'запрос для создания вещи' из входящих запросов на API приложения
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRequestDtoCreate(
        @NotBlank(message = "Описание требуемой вещи не может быть пустым или отсутствовать")
        @Length(message = "Описание требуемой вещи имеет имеет недопустимый размер", min = 1, max = 255)
        String description
) implements Serializable, ValidatedEntity {
}