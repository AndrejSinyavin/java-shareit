package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO только для создания комментария, приходит с фронта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDtoCreate(
        @Size(message = "Размер комментария должен быть 1-1024 символа", min = 1, max = 1024)
        @NotBlank(message = "Комментарий не может быть пустым или отсутствовать")
        String text

) implements Serializable {
}