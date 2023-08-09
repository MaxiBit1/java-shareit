package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTakedDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDto bookingDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        User user = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        User user1 = User.builder()
                .id(2L)
                .name("aaa")
                .email("aaaaaaa@mail.com")
                .build();
        Item item = Item.builder()
                .id(2L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(user)
                .requestId(null)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .item(item)
                .booker(user1)
                .status(StatusBooking.WAITING)
                .start(LocalDateTime.of(2030, 12, 1, 23, 0))
                .end(LocalDateTime.of(2031, 12, 1, 23, 0))
                .build();
    }

    @Test
    void createBooking() throws Exception {
        BookingTakedDto bookingTakedDto = new BookingTakedDto();
        bookingTakedDto.setStart(bookingDto.getStart());
        bookingTakedDto.setEnd(bookingDto.getEnd());
        bookingTakedDto.setItemId(bookingDto.getItem().getId());
        Mockito
                .when(bookingService.createBooking(any(BookingTakedDto.class), anyLong()))
                .thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingTakedDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$.end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$.end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$.start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$.start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$.start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void updateBooking() throws Exception {
        bookingDto.setStatus(StatusBooking.APPROVED);
        Mockito
                .when(bookingService.updateBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$.end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$.end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$.start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$.start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$.start[2]").value(bookingDto.getStart().getDayOfMonth()));

    }

    @Test
    void getBooking() throws Exception {
        Mockito
                .when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$.end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$.end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$.start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$.start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$.start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUser() throws Exception {
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?state=FUTURE&from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwner() throws Exception {
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?state=FUTURE&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUserStateCurrent() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2022, 12, 1, 23, 0));
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?state=CURRENT&from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwnerStateCurrent() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2022, 12, 1, 23, 0));
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?state=CURRENT&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUserStatePast() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2021, 12, 1, 23, 0));
        bookingDto.setEnd(LocalDateTime.of(2022, 12, 1, 23, 0));
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?state=PAST&from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwnerStatePast() throws Exception {
        bookingDto.setStart(LocalDateTime.of(2021, 12, 1, 23, 0));
        bookingDto.setEnd(LocalDateTime.of(2022, 12, 1, 23, 0));
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?state=PAST&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUserStateWaiting() throws Exception {
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?state=WAITING&from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwnerStateWaiting() throws Exception {
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?state=WAITING&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUserStateRejected() throws Exception {
        bookingDto.setStatus(StatusBooking.REJECTED);
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?state=REJECTED&from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwnerStateRejected() throws Exception {
        bookingDto.setStatus(StatusBooking.REJECTED);
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?state=REJECTED&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentUserStateAll() throws Exception {
        bookingDto.setStatus(StatusBooking.REJECTED);
        Mockito
                .when(bookingService.getBookingsWithCurrentUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings?from=1&size=1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getBookingByCurrentOwnerStateAll() throws Exception {
        bookingDto.setStatus(StatusBooking.REJECTED);
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));
        mvc.perform(get("/bookings/owner?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingDto.getStatus().toString()))
                .andExpect(jsonPath("$[0].item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$[0].end[0]").value(bookingDto.getEnd().getYear()))
                .andExpect(jsonPath("$[0].end[1]").value(bookingDto.getEnd().getMonthValue()))
                .andExpect(jsonPath("$[0].end[2]").value(bookingDto.getEnd().getDayOfMonth()))
                .andExpect(jsonPath("$[0].start[0]").value(bookingDto.getStart().getYear()))
                .andExpect(jsonPath("$[0].start[1]").value(bookingDto.getStart().getMonthValue()))
                .andExpect(jsonPath("$[0].start[2]").value(bookingDto.getStart().getDayOfMonth()));
    }

    @Test
    void getOwnerWrongState() throws Exception {
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());
        mvc.perform(get("/bookings/owner?state=AAAA&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getWrongState() throws Exception {
        Mockito
                .when(bookingService.getBookingWithCurrentOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());
        mvc.perform(get("/bookings/?state=AAAA&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}