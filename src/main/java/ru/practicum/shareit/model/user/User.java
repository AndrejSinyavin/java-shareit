package ru.practicum.shareit.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.ValidatedEntity;

/**
 * Сущность 'пользователь'
 */
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements ValidatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @DecimalMin(value = "0", message = "ID не может быть отрицательным значением")
    @NotNull
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Email(message = "Неверный формат для email")
    @NotNull(message = "Не указан email")
    @Column(name = "email", nullable = false)
    String email;

}
