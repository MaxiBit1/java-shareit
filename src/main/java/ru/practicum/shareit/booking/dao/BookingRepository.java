package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс для работы с бд бронирований
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndEndIsBefore(long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndStatusBookingIs(long bookerId, StatusBooking statusBooking, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBefore(long bookerId, LocalDateTime end, LocalDateTime start, Sort sort);

    List<Booking> findByBookerId(long bookerId, Sort sort);

    List<Booking> findByItemUserIdAndEndIsBefore(long userId, LocalDateTime end, Sort sort);

    List<Booking> findByItemUserIdAndStatusBookingIs(long userId, StatusBooking statusBooking, Sort sort);

    List<Booking> findByItemUserIdAndStartIsAfter(long userId, LocalDateTime start, Sort sort);

    List<Booking> findByItemUserIdAndEndIsAfterAndStartIsBefore(long userId, LocalDateTime end, LocalDateTime start, Sort sort);

    List<Booking> findByItemUserId(long userId, Sort sort);

    List<Booking> findByItemIdAndStatusBookingIs(long itemId, StatusBooking statusBooking);

}
