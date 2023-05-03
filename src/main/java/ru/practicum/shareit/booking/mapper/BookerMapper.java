package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.booking.model.Booking;

/**
 * Класс-маппер для брони
 */
public class BookerMapper {

    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatusBooking())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .build();
    }

    public static BookingDtoToItem toBookingItem(Booking booking) {
        return BookingDtoToItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();

    }
}
