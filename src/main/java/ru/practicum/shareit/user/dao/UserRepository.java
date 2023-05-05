package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

/**
 * Интерфейс для работы с Бд юзеров
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
