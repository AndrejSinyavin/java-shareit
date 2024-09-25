package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO для {@link ru.practicum.shareit.model.item.Comment}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDto(
        Long id,
        String authorName,
        String text,
        LocalDateTime created
) implements Serializable {
}