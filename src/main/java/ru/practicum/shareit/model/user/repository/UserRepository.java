package ru.practicum.shareit.model.user.repository;

import ru.practicum.shareit.model.user.entity.User;

public interface UserRepository {

    public User get(Long id);

    public User create(User user);

    public User update(User user, Long id);

    public void delete(Long id);

}
