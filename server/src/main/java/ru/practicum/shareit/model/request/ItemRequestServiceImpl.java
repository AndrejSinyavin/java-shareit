package ru.practicum.shareit.model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.item.ItemRepository;
import ru.practicum.shareit.model.item.dto.ItemDtoShort;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoWithAnswer;
import ru.practicum.shareit.model.user.UserRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Реализация интерфейса {@link ItemRequestService} для работы с 'запросами на добавление вещей'
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    static String REQUEST_NOT_FOUND = "'Запрос на предмет' не найден в репозитории";
    String thisService = this.getClass().getSimpleName();
    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;

    /**
     * Метод создает запрос на добавление вещи
     * @param data шаблон для создания запроса {@link ItemRequest}
     * @param requesterId идентификатор автора запроса
     * @return сформированный запрос
     */
    @Override
    public ItemRequest add(ItemRequest data, Long requesterId) {
        var requester = userRepository.findById(requesterId).orElseThrow(() ->
                new EntityNotFoundException(thisService, USER_NOT_FOUND, requesterId.toString()));
        data.setRequester(requester);
        data.setCreated(Instant.now(Clock.systemUTC()));
        return itemRequestRepository.save(data);
    }

    /**
     * Метод возвращает запрос на аренду по его идентификатору и список предложенных вещей
     * @param requestId идентификатор запроса
     * @param ownerId автор запроса
     * @return содержимое запроса и список предложений
     */
    @Override
    public ItemRequestDtoWithAnswer getItemRequest(Long requestId, Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, ownerId.toString());
        }
        var request = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException(thisService, REQUEST_NOT_FOUND, requestId.toString())
        );
        var offersList = itemRepository.findByRequestOrderByOwner_IdAsc(requestId)
                .stream()
                .map(item -> new ItemDtoShort(item.getId(), item.getName(), item.getOwner().getId()))
                .toList();
        return new ItemRequestDtoWithAnswer(requestId, request.getDescription(), request.getCreated(), offersList);
    }

    /**
     * Метод возвращает список всех запросов на аренду от пользователя со списками предложений к ним
     *
     * @param ownerId идентификатор создатель пользователя
     * @return список всех запросов пользователя со списками предложений к ним
     */
    @Override
    public List<ItemRequestDtoWithAnswer> getAllOwnersRequests(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, ownerId.toString());
        }
        HashMap<Long, ItemRequestDtoWithAnswer> relation = new HashMap<>();
        var requestList = itemRequestRepository.findByRequester_IdOrderByCreatedDesc(ownerId);
        var requestIdsList = requestList
                .stream()
                .peek(item -> {
                    item.setItems(new ArrayList<>());
                    relation.put(item.getId(), item);
                })
                .map(ItemRequestDtoWithAnswer::getId)
                .toList();
        var offersList = itemRepository.findByRequestIn(requestIdsList);
        offersList.forEach(item -> {
            var id = item.getRequest();
            relation.get(id).getItems().add(
                    new ItemDtoShort(item.getId(), item.getName(), item.getOwner().getId()));
        });
        return requestList;
    }

    /**
     * Метод возвращает список всех запросов на аренду от всех пользователей
     *
     * @param ownerId идентификатор пользователя, который хочет получить список
     * @return список всех запросов на аренду от всех пользователей
     */
    @Override
    public List<ItemRequest> getAllRequest(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException(thisService, USER_NOT_FOUND, ownerId.toString());
        }
        return itemRequestRepository.findAll();
    }
}
