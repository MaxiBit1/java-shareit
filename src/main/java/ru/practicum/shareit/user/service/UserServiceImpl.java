package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.EmailExist;
import ru.practicum.shareit.exceptions.model.NoObjectExist;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * класс реализующий интерфейс UserService
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(User user) {
        log.info("User was created");
        return UserMapper.getUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        User oldUser = userRepository.findById(userId).get();
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(oldUser.getEmail()) || checkEmail(user.getEmail())) {
                throw new EmailExist();
            }
            oldUser.setEmail(user.getEmail());
        }
        log.info("User was updated");
        return UserMapper.getUserDto(userRepository.save(oldUser));
    }

    @Override
    public void deleteUser(long userId) {
        log.info("User was deleted");
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("List users found");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long userId) {
        log.info("User was found");
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return UserMapper.getUserDto(user.get());
        }
        throw new NoObjectExist();
    }

    /**
     * Метод для проверки mail
     *
     * @param email почта
     * @return результат проверки
     */

    private boolean checkEmail(String email) {
        return userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .anyMatch(userEmail -> userEmail.equals(email));
    }
}
