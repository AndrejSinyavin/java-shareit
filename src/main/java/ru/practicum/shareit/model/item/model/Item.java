package ru.practicum.shareit.model.item.model;

import lombok.Data;
import ru.practicum.shareit.model.request.ItemRequest;
import ru.practicum.shareit.model.user.entity.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    User owner;
    ItemRequest request;
}