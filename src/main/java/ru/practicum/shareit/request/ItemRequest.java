package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

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
