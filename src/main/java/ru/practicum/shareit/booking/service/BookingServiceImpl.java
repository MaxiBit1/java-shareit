package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.StateOfBookingCurrentUser;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTakedDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.model.NoUserExist;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingTakedDto booking, long userId) {
        Optional<Item> itemOptional = itemRepository.findById(booking.getItemId());
        if (itemOptional.isEmpty()) {
            throw new NoUserExist();
        }
        if (!itemOptional.get().getAvailable()) {
            throw new ValidationException();
        }
        Item item = itemOptional.get();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoUserExist();
        }
        User user = userOptional.get();
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start.isAfter(end)
                || start.isEqual(end)
                || start.isBefore(LocalDateTime.now())) {
            throw new ValidationException();
        }
        if (userId == item.getId()) {
            throw new NoUserExist();
        }
        return BookingMapper.toDto(bookingRepository.save(Booking.builder()
                .booker(user)
                .statusBooking(StatusBooking.WAITING)
                .start(start)
                .end(end)
                .item(item)
                .build()));
    }

    @Override
    public BookingDto updateBooking(long bookingId, boolean approved, long userId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NoUserExist();
        }
        Booking booking = bookingOptional.get();
        if (booking.getItem().getUser().getId() != userId) {
            throw new NoUserExist();
        }
        if (booking.getStatusBooking() == StatusBooking.APPROVED) {
            throw new ValidationException();
        }
        if (approved) {
            booking.setStatusBooking(StatusBooking.APPROVED);
        } else {
            booking.setStatusBooking(StatusBooking.REJECTED);
        }
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(long bookingId, long userId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new NoUserExist();
        }
        Booking booking = bookingOptional.get();
        if ((booking.getItem().getUser().getId() != userId) == (booking.getBooker().getId() != userId)) {
            throw new NoUserExist();
        }
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsWithCurrentUser(String state, long userId) {
        List<BookingDto> bookingDtos;
        StateOfBookingCurrentUser state1 = StateOfBookingCurrentUser.valueOf(state);
        switch (state1) {
            case CURRENT:
                bookingDtos = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingDtos = bookingRepository.findByBookerIdAndEndIsBefore(userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingDtos = bookingRepository.findByBookerIdAndStartIsAfter(userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookingDtos = bookingRepository.findByBookerIdAndStatusBookingIs(userId,
                                StatusBooking.WAITING,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingDtos = bookingRepository.findByBookerIdAndStatusBookingIs(userId,
                                StatusBooking.REJECTED,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            default:
                bookingDtos = bookingRepository.findByBookerId(userId, Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
        }
        if (bookingDtos.isEmpty()) {
            bookingDtos = new ArrayList<>();
        }
        return bookingDtos;
    }

    @Override
    public List<BookingDto> getBookingWithCurrentOwner(String state, long userId) {
        List<BookingDto> bookingDtos;
        StateOfBookingCurrentUser state1 = StateOfBookingCurrentUser.valueOf(state);
        switch (state1) {
            case CURRENT:
                bookingDtos = bookingRepository.findByItemUserIdAndEndIsAfterAndStartIsBefore(userId,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingDtos = bookingRepository.findByItemUserIdAndEndIsBefore(userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingDtos = bookingRepository.findByItemUserIdAndStartIsAfter(userId,
                                LocalDateTime.now(),
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookingDtos = bookingRepository.findByItemUserIdAndStatusBookingIs(userId,
                                StatusBooking.WAITING,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingDtos = bookingRepository.findByItemUserIdAndStatusBookingIs(userId,
                                StatusBooking.REJECTED,
                                Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
            default:
                bookingDtos = bookingRepository.findByItemUserId(userId, Sort.by(Sort.Direction.DESC, "start"))
                        .stream()
                        .map(BookingMapper::toDto)
                        .collect(Collectors.toList());
                break;
        }
        if (bookingDtos.isEmpty()) {
            bookingDtos = new ArrayList<>();
        }
        return bookingDtos;
    }
}
