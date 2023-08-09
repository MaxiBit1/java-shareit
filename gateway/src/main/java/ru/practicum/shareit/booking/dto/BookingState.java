package ru.practicum.shareit.booking.dto;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState checkBooking(String state) {
        return Arrays.stream(values())
                .filter(state1 -> state.equalsIgnoreCase(state1.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
    }
}
