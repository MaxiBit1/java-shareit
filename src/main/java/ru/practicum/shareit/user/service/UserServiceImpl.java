package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * класс реализующий интерфейс UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        return userRepository.update(userId, user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getListUsers();
    }

    @Override
    public UserDto getUser(long userId) {
        return userRepository.getUser(userId);
    }
}
