package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryInMemoryImp implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long idCount = 0L;

    /**
     * @param id
     * @return
     */
    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    /**
     * @param user
     * @return
     */
    @Override
    public Long createUser(User user) {
        var id = getNewId();
        user.setId(id);
        users.put(id, user);
        return id;
    }


    /**
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    /**
     * @param id
     */
    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private Long getNewId() {
        return ++idCount;
    }

}
