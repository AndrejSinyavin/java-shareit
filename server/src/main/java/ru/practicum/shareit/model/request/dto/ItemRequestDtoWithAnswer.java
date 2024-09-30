package ru.practicum.shareit.model.request.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

/**
 * DTO для {@link ru.practicum.shareit.model.request.ItemRequest}. Для возврата на фронт данных о запросе
 * и списка полученных предложений
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDtoWithAnswer implements Serializable {
    Long id;
    String description;
    Instant created;
    Collection<ItemDtoShort> items;

}