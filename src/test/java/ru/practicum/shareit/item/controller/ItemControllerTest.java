package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private Item item;
    private User user;
    private ItemDto itemDto;
    private ItemDtoWithDate itemDtoWithDate;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        item = Item.builder()
                .id(1L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(user)
                .requestId(null)
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("aaaaa")
                .user(user)
                .created(LocalDateTime.now())
                .item(item)
                .build();
        commentDto = CommentMapper.toDto(comment);
        itemDtoWithDate = ItemDtoWithDate.builder()
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .comments(List.of(commentDto))
                .build();
        itemDto = ItemMapper.toItemDto(item);
        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void add() throws Exception {
        ItemDtoWithRequest itemDtoWithRequest
                = ItemDtoWithRequest.builder()
                .id(1L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .requestId(null)
                .build();
        Mockito
                .when(itemService.addItem(any(ItemDtoWithRequest.class), anyLong()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDtoWithRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.isAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()));
    }

    @Test
    void update() throws Exception {
        Item item1 = Item.builder()
                .id(1L)
                .name("fffff")
                .description("aaasadsadsad")
                .available(false)
                .requestId(null)
                .user(user)
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("fffff")
                .description("aaasadsadsad")
                .available(false)
                .requestId(null)
                .build();
        Mockito
                .when(itemService.updateItem(anyLong(), any(Item.class), anyLong()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(item1))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.isAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDto.getRequestId()));
    }

    @Test
    void getItem() throws Exception {
        Mockito
                .when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDtoWithDate);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoWithDate.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoWithDate.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoWithDate.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoWithDate.isAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDtoWithDate.getRequestId()));
    }

    @Test
    void getItems() throws Exception {
        Mockito
                .when(itemService.getItems(anyLong(), anyLong(), anyLong()))
                .thenReturn(List.of(itemDtoWithDate));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoWithDate.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDtoWithDate.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDtoWithDate.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDtoWithDate.isAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(itemDtoWithDate.getRequestId()));
    }

    @Test
    void getSearchedItem() throws Exception {
        String text = "iiii";
        Mockito
                .when(itemService.getCurrentItems(any(String.class), anyLong(), anyLong()))
                .thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text=" + text)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.isAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(itemDto.getRequestId()));
    }

    @Test
    void createComment() throws Exception {
        Mockito
                .when(itemService.createComment(anyLong(), anyLong(), any(Comment.class)))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created[0]").value(commentDto.getCreated().getYear()))
                .andExpect(jsonPath("$.created[1]").value(commentDto.getCreated().getMonthValue()))
                .andExpect(jsonPath("$.created[2]").value(commentDto.getCreated().getDayOfMonth()));
    }
}