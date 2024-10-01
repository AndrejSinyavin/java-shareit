package ru.practicum.shareit.model.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * DTO для создания DAO {@link ru.practicum.shareit.model.request.ItemRequest} из входящих запросов на API приложения
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRequestDtoCreate(
        String description
) implements Serializable {
}