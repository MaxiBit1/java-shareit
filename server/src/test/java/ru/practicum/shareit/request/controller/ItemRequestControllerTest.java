package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;
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
                .description("aaaaa")
                .created(LocalDateTime.now())
                .user(user)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("aaaaa")
                .created(LocalDateTime.now())
                .build();
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveItemRequest() throws Exception {
        Mockito
                .when(itemRequestService.save(any(ItemRequest.class), anyLong(), any(LocalDateTime.class)))
                .thenReturn(itemRequestDto);
        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("aaaaa")
                .build();
        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequest1))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.created[0]").value(itemRequestDto.getCreated().getYear()))
                .andExpect(jsonPath("$.created[1]").value(itemRequestDto.getCreated().getMonthValue()))
                .andExpect(jsonPath("$.created[2]").value(itemRequestDto.getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getItemRequests() throws Exception {
        Mockito
                .when(itemRequestService.getRequests(anyLong()))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].created[0]").value(itemRequestDto.getCreated().getYear()))
                .andExpect(jsonPath("$[0].created[1]").value(itemRequestDto.getCreated().getMonthValue()))
                .andExpect(jsonPath("$[0].created[2]").value(itemRequestDto.getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getItemRequest() throws Exception {
        Mockito
                .when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.created[0]").value(itemRequestDto.getCreated().getYear()))
                .andExpect(jsonPath("$.created[1]").value(itemRequestDto.getCreated().getMonthValue()))
                .andExpect(jsonPath("$.created[2]").value(itemRequestDto.getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getItemRequestsPagination() throws Exception {
        Mockito
                .when(itemRequestService.getRequestsPageable(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDto));
        mvc.perform(get("/requests/all?from=0&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].created[0]").value(itemRequestDto.getCreated().getYear()))
                .andExpect(jsonPath("$[0].created[1]").value(itemRequestDto.getCreated().getMonthValue()))
                .andExpect(jsonPath("$[0].created[2]").value(itemRequestDto.getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()));
    }
}