package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.model.user.dto.UserDto;
import ru.practicum.shareit.model.user.dto.UserDtoCreate;
import ru.practicum.shareit.model.user.dto.UserDtoUpdate;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService} для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    static String USER_ID = "ID ";
    static String USER_NOT_FOUND = "'Пользователь' не найден в репозитории";
    static String EMAIL_ALREADY_EXISTS = "Такой email уже существует ";
    String thisService = this.getClass().getSimpleName();
    UserRepository userRepository;
    UserMapper userMapper;

    /**
     * Получение 'пользователя' по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return {@link User}
     */
    @Override
    public UserDto get(Long userId) {
        var result = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString())));
        return userMapper.toUserDto(result);
    }

    /**
     * Добавление 'пользователя'
     *
     * @param userDto с необходимыми установленными полями
     * @return {@link User} с установленным ID
     */
    @Override
    public UserDto add(UserDtoCreate userDto) {
        var user = userMapper.toAddUser(userDto);
        String email = user.getEmail();
        userRepository.findByEmailContainingIgnoreCase(email).ifPresent(e -> {
            throw new EntityAlreadyExistsException(thisService, EMAIL_ALREADY_EXISTS, email);
        });
        return userMapper.toUserDto(userRepository.save(user));
    }

    /**
     * Обновление существующего 'пользователя'
     *
     * @param userDto шаблон с необходимыми установленными полями
     * @param userId идентификатор целевого 'пользователя'
     * @return пользователь с обновленным идентификатором
     */
    @Override
    public UserDto update(UserDtoUpdate userDto, Long userId) {
        var user = userMapper.toUpdateUser(userDto);
        var targetUser = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString()))
        );
        String email = user.getEmail();
        if (email != null && email.compareToIgnoreCase(targetUser.getEmail()) != 0) {
            userRepository.findByEmailContainingIgnoreCase(email).ifPresent(e -> {
                throw new EntityAlreadyExistsException(thisService, EMAIL_ALREADY_EXISTS, email);
            });
            targetUser.setEmail(email);
        }
        Optional.ofNullable(user.getName()).ifPresent(targetUser::setName);
        return userMapper.toUserDto(userRepository.save(targetUser));
    }

    /**
     * Удаление 'пользователя'
     *
     * @param userId идентификатор 'пользователя'
     */
    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

}
