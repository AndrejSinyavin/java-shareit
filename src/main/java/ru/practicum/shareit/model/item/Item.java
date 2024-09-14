package ru.practicum.shareit.model.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.model.user.User;
import ru.practicum.shareit.validation.ValidatedEntity;

/**
 * Сущность 'вещь'
 */
@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item implements ValidatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
    @NotNull
    Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Не указано название вещи")
    String name;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "Не указано описание вещи")
    String description;

    @Column(name = "available", nullable = false)
    @NotNull(message = "Не указана доступность вещи")
    Boolean available;

    @NotNull(message = "Не указан владелец вещи")
    @ManyToOne()
    @JoinColumn(name = "owner")
    User owner;

    @Column(name = "request", nullable = false)
    Long request;
}