package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Класс создающий объетка DTO
 */
//@Component
@UtilityClass
public class UserMapper {

    /**
     * Метод сохдающий объетка DTO
     *
     * @param user - юзер
     * @return - объект DTO
     */
    public static UserDto getUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
