package ru.practicum.shareit.model.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryInMemoryImp implements UserRepository {
    private final String thisService = this.getClass().getSimpleName();
    private final Map<Long, User> users = new HashMap<>();
    private Long idCount = 0L;

    /**
     * @param id
     * @return
     */
    @Override
    public User get(Long id) {
        var user = users.get(id);
        if (user == null) {
            throw new EntityNotFoundException(thisService, "Пользователь с указанным ID не найден", "");
        } else {
            return user;
        }
    }

    /**
     * @param sample
     * @return
     */
    @Override
    public User create(User sample) {
        checkConsistency(sample);
        var id = getNewId();
        var user = new User(id, sample.getName(), sample.getEmail());
        users.put(id, user);
        return user;
    }


    /**
     * @param sample
     * @return
     */
    @Override
    public User update(User sample, Long id) {
        if (!users.containsKey(id)) {
            throw new EntityNotFoundException(thisService, "Пользователь с указанным ID не найден", "");
        }
        checkConsistency(sample);
        User user = users.get(id);
        Optional.ofNullable(sample.getName()).ifPresent(user::setName);
        Optional.ofNullable(sample.getEmail()).ifPresent(user::setEmail);
        return user;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private Long getNewId() {
        return ++idCount;
    }

    private void checkConsistency(User user) {
        String email = user.getEmail();
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
            throw new EntityAlreadyExistsException(thisService, email, "Пользователь с таким email уже существует");
        }
    }

}
