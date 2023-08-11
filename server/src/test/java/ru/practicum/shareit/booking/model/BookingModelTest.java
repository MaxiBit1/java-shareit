package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingModelTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void shouldSerialiseJsonBooking() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .status(StatusBooking.WAITING)
                .start(LocalDateTime.of(2030, 11, 2, 3, 10, 10))
                .end(LocalDateTime.of(2033, 9, 12, 10, 10, 10))
                .build();
        JsonContent<BookingDto> jsonContext = json.write(bookingDto);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingDto.getStatus().toString());
        assertThat(jsonContext).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDto.getStart().toString());
        assertThat(jsonContext).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingDto.getEnd().toString());
    }
}
