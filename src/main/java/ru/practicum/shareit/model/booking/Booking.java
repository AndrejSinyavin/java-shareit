package ru.practicum.shareit.model.booking;

import lombok.Data;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;

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