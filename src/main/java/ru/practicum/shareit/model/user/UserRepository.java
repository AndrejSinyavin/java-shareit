package ru.practicum.shareit.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailContainingIgnoreCase(String email);

}
