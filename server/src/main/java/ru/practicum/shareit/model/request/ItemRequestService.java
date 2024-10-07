package ru.practicum.shareit.model.request;

import ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer;

import java.util.List;

/**
 * Интерфейс сервисов для работы с 'запросами на добавление вещей'
 */
public interface ItemRequestService {

    ItemRequest add(ItemRequest itemRequest, Long ownerId);

    ItemRequestDtoWithAnswer getItemRequest(Long requestId, Long ownerId);

    List<ItemRequestDtoWithAnswer> getAllOwnersRequests(Long ownerId);

    List<ItemRequest> getAllRequest(Long ownerId);

}
