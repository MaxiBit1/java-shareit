package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Класс создающий объетка DTO
 */
@Component
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

    /**
     * Метод преобразующий из объекта DTO в объект User
     *
     * @param userDto - DTO юзера
     * @return - объект User
     */
    public static User toNewUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
