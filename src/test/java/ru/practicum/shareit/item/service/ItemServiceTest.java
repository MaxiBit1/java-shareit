package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.model.NoObjectExist;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    private User user;
    private Item item;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;


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
        itemService
                = new ItemServiceImpl(itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);
    }

    @Test
    void addItem() {
        Item newItem = Item.builder()
                .id(1L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .build();
        ItemDtoWithRequest itemDtoWithRequest
                = new ItemDtoWithRequest(1L, "iiii", "ooooooooasdasfa", true, null);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        newItem.setUser(user);

        newItem.setRequestId(null);
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        ItemDto newItemDto = itemService.addItem(itemDtoWithRequest, 1L);
        assertEquals(item.getId(), newItemDto.getId());
        assertEquals(item.getName(), newItemDto.getName());
        assertEquals(item.getDescription(), newItemDto.getDescription());
        assertEquals(item.getAvailable(), newItemDto.isAvailable());
        assertNull(newItemDto.getRequestId());
    }

    @Test
    void updateItem() {
        Item newItem = Item.builder()
                .id(1L)
                .name("lllll")
                .description("asdasdfsa")
                .available(false)
                .build();
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        item.setAvailable(newItem.getAvailable());
        Mockito
                .when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        ItemDto newItemDto = itemService.updateItem(1L, newItem, 1L);
        assertEquals(item.getId(), newItemDto.getId());
        assertEquals(item.getName(), newItemDto.getName());
        assertEquals(item.getDescription(), newItemDto.getDescription());
        assertEquals(item.getAvailable(), newItemDto.isAvailable());
    }

    @Test
    void getItem() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        ItemDtoWithDate itemDtoWithDate = itemService.getItem(1L, 1L);
        assertEquals(item.getId(), itemDtoWithDate.getId());
        assertEquals(item.getName(), itemDtoWithDate.getName());
        assertEquals(item.getDescription(), itemDtoWithDate.getDescription());
        assertEquals(item.getAvailable(), itemDtoWithDate.isAvailable());
    }

    @Test
    void getItems() {
        ItemDtoWithDate itemDtoWithDate1 = ItemDtoWithDate.builder()
                .id(1L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .requestId(null)
                .build();
        List<ItemDtoWithDate> itemDtos = List.of(itemDtoWithDate1);
        List<Item> items = List.of(item);
        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findAllByUser(user))
                .thenReturn(items);
        List<ItemDtoWithDate> resultList = itemService.getItems(1L, 0, 0);
        assertEquals(itemDtos.get(0).getId(), resultList.get(0).getId());
        assertEquals(itemDtos.get(0).getName(), resultList.get(0).getName());
        assertEquals(itemDtos.get(0).getDescription(), resultList.get(0).getDescription());
        assertEquals(itemDtos.get(0).isAvailable(), resultList.get(0).isAvailable());

    }

    @Test
    void getCurrentItems() {
        String text = "iiii";
        List<ItemDto> itemDtos = List.of(ItemMapper.toItemDto(item));
        Mockito
                .when(itemRepository.findItemsByText(anyString()))
                .thenReturn(itemDtos);
        List<ItemDto> resultList = itemService.getCurrentItems(text, 0, 0);
        assertEquals(itemDtos.get(0).getId(), resultList.get(0).getId());
        assertEquals(itemDtos.get(0).getName(), resultList.get(0).getName());
        assertEquals(itemDtos.get(0).getDescription(), resultList.get(0).getDescription());
        assertEquals(itemDtos.get(0).isAvailable(), resultList.get(0).isAvailable());
    }

    @Test
    void createComment() {
        Booking booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user)
                .statusBooking(StatusBooking.APPROVED)
                .start(LocalDateTime.now().minusDays(1L))
                .end(LocalDateTime.now().plusDays(1L))
                .build();
        List<Booking> bookings = List.of(booking);
        Comment atolonComent = Comment.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .text("good")
                .item(item)
                .user(user)
                .build();
        Comment comment = Comment.builder()
                .text("good")
                .build();
        Mockito
                .when(bookingRepository.findByItemIdAndStatusBookingIs(anyLong(), any(StatusBooking.class)))
                .thenReturn(bookings);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(commentRepository.save(any(Comment.class)))
                .thenReturn(atolonComent);
        CommentDto commentDto = itemService.createComment(1L, 1L, comment);
        assertEquals(atolonComent.getId(), commentDto.getId());
        assertEquals(atolonComent.getText(), commentDto.getText());
        assertEquals(atolonComent.getUser().getName(), commentDto.getAuthorName());
        assertEquals(atolonComent.getCreated(), commentDto.getCreated());
    }

    @Test
    void getItemsByRequestId() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .user(user)
                .created(LocalDateTime.now())
                .description("1111")
                .build();
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        item.setRequestId(itemRequest);
        List<Item> items = List.of(item);
        Mockito
                .when(itemRepository.findAllByRequestId(any(ItemRequest.class)))
                .thenReturn(items);
        List<ItemDto> resultList = itemService.getItemsByRequestId(1L);
        assertEquals(items.get(0).getId(), resultList.get(0).getId());
        assertEquals(items.get(0).getName(), resultList.get(0).getName());
        assertEquals(items.get(0).getDescription(), resultList.get(0).getDescription());
        assertEquals(items.get(0).getRequestId().getId(), resultList.get(0).getRequestId());
    }

    @Test
    void getNotItem() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenThrow(new NoObjectExist());

        final NoObjectExist exception = assertThrows(
                NoObjectExist.class,
                () -> itemService.getItem(100, 1)
        );
    }

    @Test
    void getPaginationWrong() {

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> itemService.getCurrentItems("aaaa", -1,-1)
        );
    }
}