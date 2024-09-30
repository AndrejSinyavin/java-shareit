package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 'Короткий' DTO для {@link ru.practicum.shareit.model.item.Item}, минимальный набор полей для составного ответа
 * фронту, составленного из нескольких DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoShort(
        Long id,
        String name,
        Long ownerId
) implements Serializable {
}