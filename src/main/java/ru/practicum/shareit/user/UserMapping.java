package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Класс создающий объетка DTO
 */
@Component
public class UserMapping {

    /**
     * Метод сохдающий объетка DTO
     *
     * @param user - юзер
     * @return - объект DTO
     */
    public UserDto getUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
