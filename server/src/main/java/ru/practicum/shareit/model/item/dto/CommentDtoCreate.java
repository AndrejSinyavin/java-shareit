package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO только для создания комментария {@link ru.practicum.shareit.model.item.Comment}, приходит с фронта
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CommentDtoCreate(
        String text
) implements Serializable {
}