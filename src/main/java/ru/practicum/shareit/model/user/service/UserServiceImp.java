package ru.practicum.shareit.model.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.mapper.UserMapper;
import ru.practicum.shareit.model.user.entity.User;
import ru.practicum.shareit.model.user.repository.UserRepository;
import ru.practicum.shareit.validate.EntityValidate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {
    UserRepository users;
    UserMapper mapper;
    EntityValidate validator;

    /**
     * @param id
     * @return
     */
    @Override
    public UserDto get(Long id) {
        var user = users.get(id);
        return mapper.toUserDto(user);
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public UserDto create(UserDto userDto) {
        var user = (User) validator.validate(mapper.toUser(userDto));
        return mapper.toUserDto(users.create(user));
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public UserDto update(UserDto userDto, Long id) {
        var sample = mapper.toUser((UserDto) validator.validate(userDto));
        return mapper.toUserDto(users.update(sample, id));
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        users.delete(id);
    }

}
