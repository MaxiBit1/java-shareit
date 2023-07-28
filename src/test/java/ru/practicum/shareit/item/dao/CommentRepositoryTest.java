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

    private Comment comment;


    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build());
        Item item = itemRepository.save(Item.builder()
                .id(1L)
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
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findByItem_id() {
        List<Comment> resultList = commentRepository.findByItem_id(1L);
        assertEquals(comment.getId(), resultList.get(0).getId());
        assertEquals(comment.getCreated(), resultList.get(0).getCreated());
        assertEquals(comment.getText(), resultList.get(0).getText());
        assertEquals(comment.getItem(), resultList.get(0).getItem());
    }
}