package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTakedDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.model.NoBookingException;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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
class BookingServiceTest {

    private User user;

    private Item item;

    private Booking booking;
    private Booking booking1;

    private User user1;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        user1 = User.builder()
                .id(2L)
                .name("aaa")
                .email("aaaaaaa@mail.com")
                .build();
        item = Item.builder()
                .id(2L)
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(user)
                .requestId(null)
                .build();
        booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user1)
                .statusBooking(StatusBooking.WAITING)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
        booking1 = Booking.builder()
                .id(2L)
                .item(item)
                .booker(user1)
                .statusBooking(StatusBooking.APPROVED)
                .start(LocalDateTime.now().minusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    @Test
    void createBooking() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingTakedDto bookingTakedDto = new BookingTakedDto();
        bookingTakedDto.setStart(booking.getStart());
        bookingTakedDto.setEnd(booking.getEnd());
        bookingTakedDto.setItemId(booking.getItem().getId());
        BookingDto bookingDto = bookingService.createBooking(bookingTakedDto, 1L);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getItem(), bookingDto.getItem());
        assertEquals(booking.getStatusBooking(), bookingDto.getStatus());
    }

    @Test
    void updateBooking() {
        Booking booking1 = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user1)
                .statusBooking(StatusBooking.APPROVED)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking1);
        BookingDto bookingDto = bookingService.updateBooking(1L, true, 1L);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getItem(), bookingDto.getItem());
        assertEquals(booking.getStatusBooking(), bookingDto.getStatus());
    }

    @Test
    void getBooking() {
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        BookingDto bookingDto = bookingService.getBooking(1L, 1L);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getItem(), bookingDto.getItem());
        assertEquals(booking.getStatusBooking(), bookingDto.getStatus());
    }

    @Test
    void getBookingsWithCurrentUser() {
        Mockito
                .when(bookingRepository.findByBookerIdAndStartIsAfter(anyLong(),
                        any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("FUTURE", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwner() {
        Mockito
                .when(bookingRepository.findByItemUserIdAndStartIsAfter(anyLong(),
                        any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("FUTURE", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingsWithCurrentUserStatusCurrent() {
        booking.setStart(LocalDateTime.now().minusDays(1L));
        Mockito
                .when(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("CURRENT", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwnerStatusCurrent() {
        booking.setStart(LocalDateTime.now().minusDays(1L));
        Mockito
                .when(bookingRepository.findByItemUserIdAndEndIsAfterAndStartIsBefore(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("CURRENT", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingsWithCurrentUserStatusPast() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().minusDays(1L));
        Mockito
                .when(bookingRepository.findByBookerIdAndEndIsBefore(anyLong(),
                        any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("PAST", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwnerStatusPast() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().minusDays(1L));
        Mockito
                .when(bookingRepository.findByItemUserIdAndEndIsBefore(anyLong(),
                        any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("PAST", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingsWithCurrentUserStatusWaiting() {
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusBookingIs(anyLong(),
                        any(StatusBooking.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("WAITING", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwnerStatusWaiting() {
        Mockito
                .when(bookingRepository.findByItemUserIdAndStatusBookingIs(anyLong(),
                        any(StatusBooking.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("WAITING", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingsWithCurrentUserStatusRejected() {
        booking.setStatusBooking(StatusBooking.REJECTED);
        Mockito
                .when(bookingRepository.findByBookerIdAndStatusBookingIs(anyLong(),
                        any(StatusBooking.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("REJECTED", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwnerStatusRejected() {
        booking.setStatusBooking(StatusBooking.REJECTED);
        Mockito
                .when(bookingRepository.findByItemUserIdAndStatusBookingIs(anyLong(),
                        any(StatusBooking.class), any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("REJECTED", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingsWithCurrentUserStatusAll() {
        Mockito
                .when(bookingRepository.findByBookerId(anyLong(),
                        any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser("ALL", 2L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getBookingWithCurrentOwnerStatusAll() {
        Mockito
                .when(bookingRepository.findByItemUserId(anyLong(),
                        any(Sort.class)))
                .thenReturn(List.of(booking));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("ALL", 1L, null, null);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void getNoBooking() {
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenThrow(new NoBookingException());

        final NoBookingException exception = assertThrows(
                NoBookingException.class,
                () -> bookingService.getBooking(1000, 1)
        );
    }

    @Test
    void getBookingsWithCurrentUserStatusCurrentWithWrongStatus() {
        booking.setStart(LocalDateTime.now().minusDays(1L));
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.getBookingsWithCurrentUser("aaaaa", 2L, 1, 1)
        );
    }

    @Test
    void getBookingWithCurrentOwnerStatusEmptyList() {
        Mockito
                .when(bookingRepository.findByItemUserId(anyLong(),
                        any(Sort.class)))
                .thenReturn(new ArrayList<>());
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("ALL", 1L, null, null);
        assertEquals(new ArrayList<>(), resultList);
    }

    @Test
    void getBookingWithCurrentOwnerStatusWithWrongSize() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.getBookingsWithCurrentUser("ALL", 2L, -1, -1)
        );
    }

    @Test
    void getBookingWithCurrentOwnerStatusAllWithPagination() {
        Mockito
                .when(bookingRepository.findByItemUserId(anyLong(),
                        any(Sort.class)))
                .thenReturn(List.of(booking, booking1));
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner("ALL", 1L, 1, 1);
        assertEquals(booking1.getId(), resultList.get(0).getId());
        assertEquals(booking1.getStart(), resultList.get(0).getStart());
        assertEquals(booking1.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking1.getBooker(), resultList.get(0).getBooker());
        assertEquals(booking1.getItem(), resultList.get(0).getItem());
        assertEquals(booking1.getStatusBooking(), resultList.get(0).getStatus());
    }

    @Test
    void updateBookingRejectStatus() {
        Booking booking1 = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user1)
                .statusBooking(StatusBooking.REJECTED)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        Mockito
                .when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking1);
        BookingDto bookingDto = bookingService.updateBooking(1L, false, 1L);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker(), bookingDto.getBooker());
        assertEquals(booking.getItem(), bookingDto.getItem());
        assertEquals(booking.getStatusBooking(), bookingDto.getStatus());
    }
}