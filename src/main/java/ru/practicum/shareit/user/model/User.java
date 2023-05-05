package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Entity
@Table(name = "shareit_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty(message = "Имя не может быть пустым")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Введите правильный email")
    @Column(name = "email")
    private String email;

}
