package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Класс для работы с бд комментриев
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByItem_id(long itemId);
}
