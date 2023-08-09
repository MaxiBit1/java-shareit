package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;

import java.util.List;

@Getter
@Setter
@Builder
public class ItemDtoWithDate {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private BookingDtoToItem lastBooking;
    private BookingDtoToItem nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
