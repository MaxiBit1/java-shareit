package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Класс для работы с бд комментриев
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItem_id(long itemId);
}
