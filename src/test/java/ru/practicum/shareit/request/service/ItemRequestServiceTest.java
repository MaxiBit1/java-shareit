package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.model.NoItemRequestException;
import ru.practicum.shareit.exceptions.model.NoUserException;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    private ItemRequest itemRequest;
    private User user;
    private ItemRequestDto itemRequestDto1;


    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRequestMapper);
        user = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("aaaaa")
                .created(LocalDateTime.of(2030, 12, 1, 15, 0))
                .user(user)
                .build();
        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .description("aaaaa")
                .created(LocalDateTime.of(2030, 12, 1, 15, 0))
                .build();
    }

    @Test
    void save() {
        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("aaaaa")
                .build();
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        ItemRequestDto itemRequestDto =
                itemRequestService.save(itemRequest1, 1L, LocalDateTime.of(2030, 12, 1, 15, 0));
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void getRequests() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        List<ItemRequestDto> resultList = itemRequestService.getRequests(1L);
        assertEquals(itemRequest.getId(), resultList.get(0).getId());
        assertEquals(itemRequest.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequest.getCreated(), resultList.get(0).getCreated());
    }

    @Test
    void getRequest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(itemRequest);
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        ItemRequestDto itemRequestDto =
                itemRequestService.getRequest(1L, 1L);
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void getRequestsPageable() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest));
        Mockito
                .when(itemRequestRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        List<ItemRequestDto> resultList = itemRequestService.getRequestsPageable(1L, 0, 1);
        assertEquals(itemRequest.getId(), resultList.get(0).getId());
        assertEquals(itemRequest.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequest.getCreated(), resultList.get(0).getCreated());
    }

    @Test
    void getNotRequestItemRequest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenThrow(new NoItemRequestException());

        final NoItemRequestException exception = assertThrows(
                NoItemRequestException.class,
                () -> itemRequestService.getRequest(1L, 1000)
        );
    }

    @Test
    void getNotRequestUser() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenThrow(new NoUserException());


        final NoUserException exception = assertThrows(
                NoUserException.class,
                () -> itemRequestService.getRequest(1000, 1L)
        );
    }

    @Test
    void notUserFoundItemRequest() {
        itemRequestDto1 = ItemRequestDto.builder()
                .id(2L)
                .description("aaaaa")
                .created(LocalDateTime.of(2030, 12, 1, 15, 0))
                .build();
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest));
        Mockito
                .when(itemRequestRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        List<ItemRequestDto> resultList = itemRequestService.getRequestsPageable(2L, 0, 1);
        assertEquals(itemRequestDto1.getId(), resultList.get(0).getId());
        assertEquals(itemRequestDto1.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequestDto1.getCreated(), resultList.get(0).getCreated());
    }

    @Test
    void getErrorRequestsPageable() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> itemRequestService.getRequest(10000, 1)
        );
    }

    @Test
    void getRequestsPageableNotUser() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> itemRequestService.getRequestsPageable(10000, 1, 1)
        );
    }

    @Test
    void getRequestForAllUsers() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(new ArrayList<>());
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        ItemRequestDto itemRequestDto =
                itemRequestService.getRequest(1L, 1L);
        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void getRequestsPageableWithSizeNull() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(itemRequestRepository.findAllByUser(any(User.class), any(Sort.class)))
                .thenReturn(List.of(itemRequest));
        Mockito
                .when(itemRequestMapper.toDto(any(ItemRequest.class)))
                .thenReturn(itemRequestDto1);
        List<ItemRequestDto> resultList = itemRequestService.getRequestsPageable(1L, null, null);
        assertEquals(itemRequest.getId(), resultList.get(0).getId());
        assertEquals(itemRequest.getDescription(), resultList.get(0).getDescription());
        assertEquals(itemRequest.getCreated(), resultList.get(0).getCreated());
    }


}