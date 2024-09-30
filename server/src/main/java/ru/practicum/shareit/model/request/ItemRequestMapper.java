package ru.practicum.shareit.model.request;

import ru.practicum.shareit.model.request.dto.ItemRequestDto;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoCreate;

/**
 * Интерфейс маппинг 'ItemRequest <-> DTO'
 */
public interface ItemRequestMapper {

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toItemRequest(ItemRequestDtoCreate itemRequestDto);

}
