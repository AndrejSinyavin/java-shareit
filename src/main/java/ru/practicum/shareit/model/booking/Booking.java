package ru.practicum.shareit.model.booking;

import lombok.Data;
import ru.practicum.shareit.model.item.model.Item;
import ru.practicum.shareit.model.user.entity.User;

import java.time.ZonedDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    ZonedDateTime start;
    ZonedDateTime end;
    Item item;
    User booker;
    BookingStatus status;
}