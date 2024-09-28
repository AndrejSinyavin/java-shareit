package ru.practicum.shareit.model.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.model.request.dto.ItemRequestDto;
import ru.practicum.shareit.model.request.dto.ItemRequestDtoCreate;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

/**
 * Реализация интерфейса {@link ItemRequestMapper}
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestMapperImpl implements ItemRequestMapper {
    static Long EMPTY_ID = 0L;

    @Override
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                LocalDateTime.ofInstant(itemRequest.getCreated(), UTC)
        );
    }


    @Override
    public ItemRequest toItemRequest(ItemRequestDtoCreate itemRequestDto) {
        return new ItemRequest(
                EMPTY_ID,
                itemRequestDto.description(),
                null,
                null
        );
    }
}
