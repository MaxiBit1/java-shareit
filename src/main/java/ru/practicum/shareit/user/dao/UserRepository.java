package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для работы с Бд юзеров
 */
public interface UserRepository {
    /**
     * Метод сохранения юзера
     *
     * @param user - юзер
     * @return - сохраненный юзер
     */
    UserDto save(User user);

    /**
     * Метод обновления юзера
     *
     * @param userId - айди юзера
     * @param user   - юзер
     * @return - обновленный юзер
     */
    UserDto update(long userId, User user);

    /**
     * Метод удаления юзера
     *
     * @param userId - айди юзера
     */
    void delete(long userId);

    /**
     * Получние списка юзеров
     *
     * @return - список юзеров
     */
    List<UserDto> getListUsers();

    /**
     * Получение определенного юзера
     *
     * @param userId - айди юзера
     * @return - юзер
     */
    UserDto getUser(long userId);

    Map<Long, User> getStorage();
}
