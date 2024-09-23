package ru.practicum.shareit.model.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.validation.ValidatedEntity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO 'вещь' {@link Item} c датами последнего бронирования и датой ближайшего запроса на бронирование
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoBooking implements Serializable, ValidatedEntity {
        @NotNull(message = "Отсутствует ID")
        @Positive(message = "ID должно быть положительным значением")
        Long id;

        @NotBlank(message = "Не указано название вещи")
        String name;

        @NotBlank(message = "Не указано описание вещи")
        String description;

        @NotNull(message = "Не указана доступность вещи")
        Boolean available;

        LocalDate previousBooking;
        LocalDate nextBooking;

}
