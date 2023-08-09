package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO брони для вещей
 */
@Getter
@Setter
@Builder
public class BookingDtoToItem {
    private long id;
    private long bookerId;
}
