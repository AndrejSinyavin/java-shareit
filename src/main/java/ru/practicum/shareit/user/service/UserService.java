package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    public UserDto getUser(Long id);

    public UserDto createUser(UserDto userDto);

    public UserDto updateUser(UserDto userDto);

    public void deleteUser(Long id);

}
