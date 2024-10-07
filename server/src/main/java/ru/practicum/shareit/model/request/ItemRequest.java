package ru.practicum.shareit.model.request;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import ru.practicum.shareit.model.user.User;

import java.time.Instant;

/**
 * Сущность 'запрос' для запроса на аренду вещи, не найденной в общем списке вещей для аренды
 */
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "description", nullable = false)
    String description;

    @JoinColumn(name = "requester", nullable = false)
    @ManyToOne()
    User requester;

    @Column(name = "created", nullable = false)
    Instant created;

}
