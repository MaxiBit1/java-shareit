package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Booking booking;
    private User userOwner;
    private User userBooker;
    private Item item;

    @BeforeEach
    void setUp() {
        User user2 = User.builder()
                .name("aaa")
                .email("u@mail.com")
                .build();
        userOwner = userRepository.save(user2);
        User user3 = User.builder()
                .name("aaa")
                .email("aaaaaaa@mail.com")
                .build();
        userBooker = userRepository.save(user3);
        Item item1 = Item.builder()
                .name("iiii")
                .description("ooooooooasdasfa")
                .available(true)
                .user(userOwner)
                .requestId(null)
                .build();
        item = itemRepository.save(item1);
        Booking booking1 = Booking.builder()
                .item(item)
                .booker(userBooker)
                .statusBooking(StatusBooking.WAITING)
                .start(LocalDateTime.now().plusDays(1L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
        booking = bookingRepository.save(booking1);
    }

    @AfterEach
    void delete() {
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByBookerIdAndEndIsBefore() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().minusDays(1L));
        List<Booking> resultList = bookingRepository.findByBookerIdAndEndIsBefore(userBooker.getId(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByBookerIdAndStatusBookingIs() {
        List<Booking> resultList = bookingRepository.findByBookerIdAndStatusBookingIs(userBooker.getId(),
                StatusBooking.WAITING,
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByBookerIdAndStartIsAfter() {
        booking.setStart(LocalDateTime.now().plusDays(2L));
        booking.setEnd(LocalDateTime.now().plusDays(3L));
        List<Booking> resultList = bookingRepository.findByBookerIdAndStartIsAfter(userBooker.getId(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByBookerIdAndEndIsAfterAndStartIsBefore() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().plusDays(3L));
        List<Booking> resultList = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userBooker.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByBookerId() {
        List<Booking> resultList = bookingRepository.findByBookerId(userBooker.getId(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemUserIdAndEndIsBefore() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().minusDays(1L));
        List<Booking> resultList = bookingRepository.findByItemUserIdAndEndIsBefore(userOwner.getId(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemUserIdAndStatusBookingIs() {
        List<Booking> resultList = bookingRepository.findByItemUserIdAndStatusBookingIs(userOwner.getId(),
                StatusBooking.WAITING,
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemUserIdAndStartIsAfter() {
        booking.setStart(LocalDateTime.now().plusDays(2L));
        booking.setEnd(LocalDateTime.now().plusDays(3L));
        List<Booking> resultList = bookingRepository.findByItemUserIdAndStartIsAfter(userOwner.getId(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemUserIdAndEndIsAfterAndStartIsBefore() {
        booking.setStart(LocalDateTime.now().minusDays(2L));
        booking.setEnd(LocalDateTime.now().plusDays(3L));
        List<Booking> resultList = bookingRepository.findByItemUserIdAndEndIsAfterAndStartIsBefore(userOwner.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemUserId() {
        List<Booking> resultList = bookingRepository.findByItemUserId(userOwner.getId(),
                Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }

    @Test
    void findByItemIdAndStatusBookingIs() {
        List<Booking> resultList = bookingRepository.findByItemIdAndStatusBookingIs(item.getId(),
                StatusBooking.WAITING);
        assertEquals(booking.getId(), resultList.get(0).getId());
        assertEquals(booking.getItem(), resultList.get(0).getItem());
        assertEquals(booking.getStatusBooking(), resultList.get(0).getStatusBooking());
        assertEquals(booking.getStart(), resultList.get(0).getStart());
        assertEquals(booking.getEnd(), resultList.get(0).getEnd());
        assertEquals(booking.getBooker(), resultList.get(0).getBooker());
    }
}