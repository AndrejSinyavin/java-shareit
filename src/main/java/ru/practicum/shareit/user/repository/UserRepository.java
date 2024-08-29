package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    public User getUser(Long id);

    public Long createUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);

}
