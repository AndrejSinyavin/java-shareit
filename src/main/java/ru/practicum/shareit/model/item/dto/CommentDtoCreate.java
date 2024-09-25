package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;

/**
 * DTO для создания {@link ru.practicum.shareit.model.item.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDtoCreate(
        @Size(message = "Размер комментария должен быть 1-1024 символа", min = 1)
        @NotBlank(message = "Комментарий не может быть пустым или отсутствовать")
        String text
) implements Serializable, ValidatedEntity {
}