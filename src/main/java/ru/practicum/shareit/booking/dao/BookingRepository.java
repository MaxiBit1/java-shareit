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
    List<Booking> findByBooker_IdAndEndIsBefore(long bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStatusBookingIs(long bookerId, StatusBooking statusBooking, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(long bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndEndIsAfterAndStartIsBefore(long bookerId, LocalDateTime end, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_id(long bookerId, Sort sort);

    List<Booking> findByItem_User_IdAndEndIsBefore(long userId, LocalDateTime end, Sort sort);

    List<Booking> findByItem_User_IdAndStatusBookingIs(long userId, StatusBooking statusBooking, Sort sort);

    List<Booking> findByItem_User_IdAndStartIsAfter(long userId, LocalDateTime start, Sort sort);

    List<Booking> findByItem_User_IdAndEndIsAfterAndStartIsBefore(long userId, LocalDateTime end, LocalDateTime start, Sort sort);

    List<Booking> findByItem_User_Id(long userId, Sort sort);

    List<Booking> findByItem_idAndStatusBookingIs(long itemId, StatusBooking statusBooking);

}
