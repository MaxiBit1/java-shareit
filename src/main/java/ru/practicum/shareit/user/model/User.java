package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private long id;
    @NotNull(message = "Имя не может быть пустым")
    private String name;
    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Введите правильный email")
    private String email;
}
