package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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