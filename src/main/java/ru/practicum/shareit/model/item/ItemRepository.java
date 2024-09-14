package ru.practicum.shareit.model.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByNameOrDescriptionIsContainingIgnoreCaseOrderById(String searchString);
}
