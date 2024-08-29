package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository users;
    private final UserMapper mapper;

    /**
     * @param id
     * @return
     */
    @Override
    public UserDto getUser(Long id) {
        return mapper.toUserDto(users.getUser(id));
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(users.createUser(mapper.toUser(userDto)));
        return userDto;
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        var user = users.updateUser(mapper.toUser(userDto));
        return mapper.toUserDto(user);
    }

    /**
     * @param id
     */
    @Override
    public void deleteUser(Long id) {
        users.deleteUser(id);
    }

}
