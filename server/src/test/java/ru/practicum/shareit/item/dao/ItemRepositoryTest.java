package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Item item;
    private Item item1;
    private User user;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .name("aaa")
                .email("u@mail.com")
                .build();
        user = userRepository.save(user1);
        ItemRequest itemRequest1 = ItemRequest.builder()
                .user(user)
                .created(LocalDateTime.now())
                .description("1111")
                .build();
        itemRequest = itemRequestRepository.save(itemRequest1);
        Item item2 = Item.builder()
                .user(user)
                .description("aaaaa")
                .name("lol")
                .requestId(itemRequest)
                .available(true)
                .build();
        Item item3 = Item.builder()
                .user(user)
                .description("lol")
                .name("aaaaaa")
                .requestId(null)
                .available(true)
                .build();
        item = itemRepository.save(item2);
        item1 = itemRepository.save(item3);
    }

    @AfterEach
    void after() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }


    @Test
    void findAllByUser() {
        List<Item> resultList = itemRepository.findAllByUser(user);
        assertEquals(item.getId(), resultList.get(0).getId());
        assertEquals(item.getName(), resultList.get(0).getName());
        assertEquals(item.getDescription(), resultList.get(0).getDescription());
        assertEquals(item.getAvailable(), resultList.get(0).getAvailable());
        assertEquals(item.getRequestId().getId(), resultList.get(0).getRequestId().getId());
        assertEquals(item.getRequestId().getDescription(), resultList.get(0).getRequestId().getDescription());
        assertEquals(item.getRequestId().getCreated(), resultList.get(0).getRequestId().getCreated());
        assertEquals(item1.getId(), resultList.get(1).getId());
        assertEquals(item1.getName(), resultList.get(1).getName());
        assertEquals(item1.getDescription(), resultList.get(1).getDescription());
        assertEquals(item1.getAvailable(), resultList.get(1).getAvailable());
        assertEquals(item1.getRequestId(), resultList.get(1).getRequestId());
    }

    @Test
    void findItemsByText() {
        String text = "lol";
        List<ItemDto> resultList = itemRepository.findItemsByText(text);
        assertEquals(item.getId(), resultList.get(0).getId());
        assertEquals(item.getName(), resultList.get(0).getName());
        assertEquals(item.getDescription(), resultList.get(0).getDescription());
        assertEquals(item.getAvailable(), resultList.get(0).isAvailable());
        assertEquals(item1.getId(), resultList.get(1).getId());
        assertEquals(item1.getName(), resultList.get(1).getName());
        assertEquals(item1.getDescription(), resultList.get(1).getDescription());
        assertEquals(item1.getAvailable(), resultList.get(1).isAvailable());
    }

    @Test
    void findAllByRequestId() {
        List<Item> resultList = itemRepository.findAllByRequestId(itemRequest);
        assertEquals(item.getId(), resultList.get(0).getId());
        assertEquals(item.getName(), resultList.get(0).getName());
        assertEquals(item.getDescription(), resultList.get(0).getDescription());
        assertEquals(item.getAvailable(), resultList.get(0).getAvailable());
        assertEquals(item.getRequestId().getId(), resultList.get(0).getRequestId().getId());
        assertEquals(item.getRequestId().getDescription(), resultList.get(0).getRequestId().getDescription());
        assertEquals(item.getRequestId().getCreated(), resultList.get(0).getRequestId().getCreated());
    }
}