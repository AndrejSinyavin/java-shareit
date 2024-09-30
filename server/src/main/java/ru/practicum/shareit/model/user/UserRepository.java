package ru.practicum.shareit.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий для работы с 'пользователями'
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Метод ищет 'пользователя' с указанным 'email'
     *
     * @param email указанный 'email'
     * @return найденный 'пользователь'
     */
    Optional<User> findByEmailContainingIgnoreCase(String email);

}
