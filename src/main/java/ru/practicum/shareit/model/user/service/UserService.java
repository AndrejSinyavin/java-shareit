package ru.practicum.shareit.model.user.service;

import ru.practicum.shareit.model.user.dto.UserDto;

public interface UserService {

    public UserDto get(Long id);

    public UserDto create(UserDto userDto);

    public UserDto update(UserDto userDto, Long id);

    public void delete(Long id);

}
