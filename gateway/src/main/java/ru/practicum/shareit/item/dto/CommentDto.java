package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для комментария, только для возврата на фронт
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDto(
        Long id,
        String authorName,
        String text,
        LocalDateTime created
) implements Serializable {
}