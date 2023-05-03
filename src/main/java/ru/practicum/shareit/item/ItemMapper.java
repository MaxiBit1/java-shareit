package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookerMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Класс создания объекта DTO
 */
@Component
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
                item.getAvailable()
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
                .nextBooking(bookingNext != null ? BookerMapper.toBookingItem(bookingNext) : null)
                .lastBooking(bookingLast != null ? BookerMapper.toBookingItem(bookingLast) : null)
                .comments(comments)
                .build();
    }

    /**
     * Метод преобразования из объекта DTO в объект item
     *
     * @param itemDto - объект DTO
     * @param user    - юзер
     * @return - объект item
     */
    public Item toNewItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .user(user)
                .build();
    }

}
