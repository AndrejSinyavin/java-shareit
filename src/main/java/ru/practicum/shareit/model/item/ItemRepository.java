package ru.practicum.shareit.model.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.model.item.dto.ItemDto;
import ru.practicum.shareit.model.item.dto.ItemDtoBooking;
import ru.practicum.shareit.model.user.User;

import java.util.Collection;
import java.util.Set;

/**
 * JPA-репозиторий для работы с 'вещами'
 */
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("""
            select i from Item i
            where i.available is true and (upper(i.name) like upper(concat('%', ?1, '%'))
            or upper(i.description) like upper(concat('%', ?1, '%')))""")
    Collection<Item> searchSubstring(String subString);

    Collection<ItemDto> getAllByOwnerOrderById(User owner);

    Set<Item> getItemsByOwnerId(Long ownerId);
}
