package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс сервиса юзера
 */
public interface UserService {
    /**
     * Метод сохранения юзера
     *
     * @param user - юзер
     * @return - сохраенненый юзер
     */
    UserDto saveUser(User user);

    /**
     * Метод обновления юзера
     *
     * @param userId - айди юзера
     * @param user   - юзер
     * @return - обновленный юзер
     */
    UserDto updateUser(long userId, User user);

    /**
     * Метод удаления юзера
     *
     * @param userId - айди юзера
     */
    void deleteUser(long userId);

    /**
     * Метод получения списка юзеров
     *
     * @return - список юзеров
     */
    List<UserDto> getUsers();

    /**
     * Метод получниея юзера по айди
     *
     * @param userId - айди юзера
     * @return - юзер
     */
    UserDto getUser(long userId);
}
