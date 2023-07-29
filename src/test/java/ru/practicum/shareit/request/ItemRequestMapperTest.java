package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @Mock
    private ItemService itemService;

    private ItemRequestMapper itemRequestMapper;

    private ItemRequestDto itemRequestDto;
    private Item item;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("fff")
                .user(user)
                .created(LocalDateTime.of(2030, 11, 2, 3, 10, 10))
                .build();
        item = Item.builder()
                .id(1L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(user)
                .requestId(itemRequest)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("fff")
                .items(List.of(ItemMapper.toItemDto(item)))
                .created(LocalDateTime.of(2030, 11, 2, 3, 10, 10))
                .build();
        itemRequestMapper = new ItemRequestMapper(itemService);
    }

    @Test
    void toDto() {
        Mockito
                .when(itemService.getItemsByRequestId(anyLong()))
                .thenReturn(List.of(ItemMapper.toItemDto(item)));
        ItemRequestDto itemRequestDto1 = itemRequestMapper.toDto(itemRequest);
        assertEquals(itemRequestDto.getId(), itemRequestDto1.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequestDto1.getDescription());
        assertEquals(itemRequestDto.getItems().get(0).getId(), itemRequestDto1.getItems().get(0).getId());
        assertEquals(itemRequestDto.getItems().get(0).getName(), itemRequestDto1.getItems().get(0).getName());
        assertEquals(itemRequestDto.getItems().get(0).getDescription(), itemRequestDto1.getItems().get(0).getDescription());
        assertEquals(itemRequestDto.getCreated(), itemRequestDto1.getCreated());
    }
}