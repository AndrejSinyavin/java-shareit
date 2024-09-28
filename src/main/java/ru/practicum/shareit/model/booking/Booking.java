package ru.practicum.shareit.model.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.model.user.User;

import java.time.Instant;

/**
 * Сущность 'бронирование'
 */
@Entity
@Table(name = "bookings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "start", nullable = false)
    Instant start;

    @Column(name = "finish", nullable = false)
    Instant end;

    @JoinColumn(name = "item", nullable = false)
    @ManyToOne()
    Item item;

    @JoinColumn(name = "booker", nullable = false)
    @ManyToOne()
    User booker;

    @Column(name = "status", nullable = false)
    @Enumerated
    BookingStatus status;

}