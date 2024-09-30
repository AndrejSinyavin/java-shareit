package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Составное DTO 'вещь' c датой последнего бронирования и датой ближайшего запроса на бронирование,
 * и списком комментариев. Только для отправления на фронт
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoBooking implements Serializable {
        Long id;
        String name;
        String description;
        Boolean available;
        LocalDateTime lastBooking;
        LocalDateTime nextBooking;
        List<CommentDto> comments;
}
