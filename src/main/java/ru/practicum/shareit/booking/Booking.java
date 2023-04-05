package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private long id;
    private long idItem;
    private LocalDateTime startTimeBooking;
    private LocalDateTime endTimeBooking;
    private long idUser;
    private StatusBooking statusBooking;
}
