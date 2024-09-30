package ru.practicum.shareit.model.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.EntityNotFoundException;

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

    /**
     * Получение 'пользователя' по его идентификатору
     *
     * @param userId идентификатор пользователя
     * @return {@link User}
     */
    @Override
    public User get(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        thisService, USER_NOT_FOUND, USER_ID.concat(userId.toString())));
    }

    /**
     * Добавление 'пользователя'
     *
     * @param user {@link User} с необходимыми установленными полями
     * @return {@link User} с установленным ID
     */
    @Override
    public User add(User user) {
        String email = user.getEmail();
        userRepository.findByEmailContainingIgnoreCase(email).ifPresent(e -> {
            throw new EntityAlreadyExistsException(thisService, EMAIL_ALREADY_EXISTS, email);
        });
        return userRepository.save(user);
    }

    /**
     * Обновление существующего 'пользователя'
     *
     * @param user шаблон {@link User} с необходимыми установленными полями
     * @param userId идентификатор целевого 'пользователя'
     * @return обновленный {@link User}
     */
    @Override
    public User update(User user, Long userId) {
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
        return userRepository.save(targetUser);
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
