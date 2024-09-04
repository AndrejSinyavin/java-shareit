package ru.practicum.shareit.model.item.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.model.item.entity.Item;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Repository
public class ItemRepositoryInMemoryImp implements ItemRepository {
    final String thisService = this.getClass().getSimpleName();
    final Map<Long, Item> items = new HashMap<>();
    Long idCount = 0L;

    @Override
    public Item add(Item sample) {
        checkConsistency(sample);
        var id = getNewId();
        var item = new Item(
                id,
                sample.getName(),
                sample.getDescription(),
                sample.getAvailable(),
                sample.getOwner(),
                sample.getRequest());
        items.put(id, item);
        return item;
    }

    private Long getNewId() {
        return ++idCount;
    }

    private void checkConsistency(Item item) {
//        String email = user.getEmail();
//        if (users.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
//            throw new EntityAlreadyExistsException(thisService, email, "Пользователь с таким email уже существует");
//        }
    }
}
