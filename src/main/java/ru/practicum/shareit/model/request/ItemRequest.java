package ru.practicum.shareit.model.request;

import lombok.Data;
import ru.practicum.shareit.model.user.entity.User;

import java.time.ZonedDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id;
    String description;
    User requester;
    ZonedDateTime created;
}
