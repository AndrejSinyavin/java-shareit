package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.practicum.shareit.model.item.Item;

import java.io.Serializable;

/**
 * DTO 'вещь' {@link Item}, используется только для создания нового DAO 'вещь'. Null поля не допустимы согласно ТЗ
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDtoCreate(
        String name,
        String description,
        Boolean available,
        Long requestId

) implements Serializable{
}