package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Класс создания объекта DTO
 */
//@Component
@UtilityClass
public class ItemMapper {

    /**
     * Метод создания объекта DTO
     *
     * @param item - вещь
     * @return - itemDto
     */
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId().getId() : null
        );
    }

    /**
     * Метод создания объекта DTO с бронированием и комментариями
     *
     * @param item - вещь
     * @return - itemDto
     */
    public static ItemDtoWithDate toItemDtoWithDate(Item item, Booking bookingNext, Booking bookingLast, List<CommentDto> comments) {
        return ItemDtoWithDate.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .nextBooking(bookingNext != null ? BookingMapper.toBookingItem(bookingNext) : null)
                .lastBooking(bookingLast != null ? BookingMapper.toBookingItem(bookingLast) : null)
                .comments(comments)
                .requestId(item.getRequestId() != null ? item.getRequestId().getId() : null)
                .build();
    }

}
