package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("aaa")
                .email("u@mail.com")
                .build());
        item = itemRepository.save(Item.builder()
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(user)
                .requestId(null)
                .build());
        comment = commentRepository.save(Comment.builder()
                .created(LocalDateTime.now())
                .text("good")
                .item(item)
                .user(user)
                .build());
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findByItem_id() {
        List<Comment> comments = commentRepository.findByItem_id(item.getId());
        assertEquals(comment.getId(), comments.get(0).getId());
        assertEquals(comment.getText(), comments.get(0).getText());
        assertEquals(comment.getCreated(), comments.get(0).getCreated());
        assertEquals(comment.getItem().getId(), comments.get(0).getItem().getId());
        assertEquals(comment.getItem().getName(), comments.get(0).getItem().getName());
        assertEquals(comment.getItem().getDescription(), comments.get(0).getItem().getDescription());
        assertEquals(comment.getItem().getAvailable(), comments.get(0).getItem().getAvailable());
        assertEquals(comment.getItem().getRequestId(), comments.get(0).getItem().getRequestId());
        assertEquals(comment.getUser().getId(), comments.get(0).getUser().getId());
        assertEquals(comment.getUser().getName(), comments.get(0).getUser().getName());
        assertEquals(comment.getUser().getEmail(), comments.get(0).getUser().getEmail());

    }
}