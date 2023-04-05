package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.model.EmailExist;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.user.UserMapping;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс реализующий интерфес UserRepository
 */
@Component
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> storageUser = new HashMap<>();
    private final UserMapping userMapping;
    private long oldSize = 0;

    @Autowired
    public UserRepositoryImpl(UserMapping userMapping) {
        this.userMapping = userMapping;
    }

    @Override
    public UserDto save(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException();
        }
        if (checkEmail(user.getEmail())) {
            throw new EmailExist();
        }
        user.setId(getId());
        storageUser.put(user.getId(), user);
        oldSize = storageUser.size();
        log.info("Пользователь создан");
        return userMapping.getUserDto(user);
    }

    @Override
    public UserDto update(long userId, User user) {
        User oldUser = storageUser.get(userId);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(oldUser.getEmail()) && checkEmail(user.getEmail())) {
                throw new EmailExist();
            }
            oldUser.setEmail(user.getEmail());
        }
        log.info("Пользователь обновлен");
        return userMapping.getUserDto(oldUser);
    }

    @Override
    public void delete(long userId) {
        log.info("Пользователь удален");
        oldSize = storageUser.size();
        storageUser.remove(userId);
    }

    @Override
    public List<UserDto> getListUsers() {
        log.info("Список пользователь");
        return storageUser.values().stream().map(userMapping::getUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long userId) {
        log.info("Получение пользователя по id");
        return userMapping.getUserDto(storageUser.get(userId));
    }

    @Override
    public Map<Long, User> getStorage() {
        return storageUser;
    }

    /**
     * Метод получния айди для юзера
     *
     * @return - айди
     */
    private long getId() {
        long lastId = oldSize;
        return lastId + 1;
    }

    /**
     * Метод сравнения по всему хранилищу email
     *
     * @param email - email
     * @return - результат проверки
     */
    private boolean checkEmail(String email) {
        return storageUser.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(userEmail -> userEmail.equals(email));
    }

}
