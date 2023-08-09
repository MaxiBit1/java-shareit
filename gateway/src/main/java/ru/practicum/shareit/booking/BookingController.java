package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingTackedDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingTackedDto booking,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create booking {}, userId={}", booking, userId);
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable("bookingId") Long bookingId,
                                                @RequestParam("approved") boolean approved,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Update bookingId={}, approved={}, userId={}", bookingId, approved, userId);
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable("bookingId") long bookingID,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get booking bookingId={}, userId={}", bookingID, userId);
        return bookingClient.getBooking(bookingID, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByCurrentUser(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                          @RequestHeader("X-Sharer-User-Id") long userId,
                                                          @RequestParam(value = "from", required = false) Integer from,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        BookingState bookingState = BookingState.checkBooking(state);
        log.info("Get bookings userId={}, state={}, from={}, size={}", userId, state, from, size);
        return bookingClient.getBookings(bookingState, userId, from, size);

    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByCurrentOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                           @RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(value = "from", required = false) Integer from,
                                                           @RequestParam(value = "size", required = false) Integer size) {
        BookingState bookingState = BookingState.checkBooking(state);
        log.info("Get bookings owner userId={}, state={}, from={}, size={}", userId, state, from, size);
        return bookingClient.getBookingsOwner(bookingState, userId, from, size);
    }
}
