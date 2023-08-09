package ru.practicum.shareit.request.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {


    private User user;

    private ItemRequest itemRequest;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("aaa")
                .email("u@mail.com")
                .build());
        itemRequest = itemRequestRepository.save(ItemRequest.builder()
                .description("aaaaa")
                .created(LocalDateTime.of(2030, 12, 1, 15, 0))
                .user(user)
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findAllByUser() {
        List<ItemRequest> resultList = itemRequestRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "created"));
        assertEquals(itemRequest.getId(), resultList.get(0).getId());
        assertEquals(itemRequest.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequest.getUser(), resultList.get(0).getUser());
        assertEquals(itemRequest.getCreated(), resultList.get(0).getCreated());
    }

    @Test
    void findByIdAndUser() {
        ItemRequest result = itemRequestRepository.findByIdAndUser(itemRequest.getId(), user);
        assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        assertEquals(itemRequest.getUser(), result.getUser());
        assertEquals(itemRequest.getCreated(), result.getCreated());
    }
}