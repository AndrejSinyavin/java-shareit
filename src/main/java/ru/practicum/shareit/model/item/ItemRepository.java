package ru.practicum.shareit.model.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.model.user.User;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(
            String searchName, String searchDescription);

    Collection<Item> getAllByOwnerOrderById(User owner);
}
