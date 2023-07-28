package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTakedDto;

import java.util.List;

/**
 * интерфейс сервиса для бронирования
 */
public interface BookingService {
    /**
     * создание бронирования
     * @param booking - получение данных бронирования
     * @param userId - юзер
     * @return - бронь
     */
    BookingDto createBooking(BookingTakedDto booking, long userId);

    /**
     * обновление статуса бронирования
     * @param bookingId - айди брони
     * @param approved - статус
     * @param userId - юзер
     * @return - обновленная бронь
     */
    BookingDto updateBooking(long bookingId, boolean approved, long userId);

    /**
     * получение брони
     * @param bookingId - айди брони
     * @param userId - юзер
     * @return - юрогь
     */
    BookingDto getBooking(long bookingId, long userId);

    /**
     * Получение списка брони для забронировавшего пользователя
     * @param state - состояние
     * @param userId - юзер
     * @return - список бронирований
     */
    List<BookingDto> getBookingsWithCurrentUser(String state, long userId, Integer from, Integer size);

    /**
     * Получение списка брони для хозяина вещей
     * @param state - состояние
     * @param userId - юзер
     * @return - список бронирований
     */
    List<BookingDto> getBookingWithCurrentOwner(String state, long userId, Integer from, Integer size);
}
