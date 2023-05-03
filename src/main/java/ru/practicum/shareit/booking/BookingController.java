package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingTakedDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс-контроллер для бронирования
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingTakedDto booking,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.createBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@PathVariable("bookingId") Long bookingId,
                                    @RequestParam("approved") boolean approved,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable("bookingId") long bookingID,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(bookingID, userId);
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookingByCurrentUser(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        List<BookingDto> resultList = bookingService.getBookingsWithCurrentUser(state, userId);
        if (resultList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingByCurrentOwner(@RequestParam(value = "state", defaultValue = "ALL") String state,
                                                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        List<BookingDto> resultList = bookingService.getBookingWithCurrentOwner(state, userId);
        if (resultList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
