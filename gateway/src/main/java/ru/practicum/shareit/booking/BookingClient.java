package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingTackedDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingTackedDto bookingTackedDto) {
        return post("", userId, bookingTackedDto);
    }

    public ResponseEntity<Object> updateBooking(long bookingId, Boolean approved, long userId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch(UriComponentsBuilder
                .newInstance()
                .path("/{bookingId}")
                .query("approved={approved}")
                .buildAndExpand(bookingId, approved)
                .toUriString(), userId, null);
    }

    public ResponseEntity<Object> getBooking(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookings(BookingState state, long userId, Integer from, Integer size) {
        if (from == null && size == null) {
            Map<String, Object> parameters = Map.of(
                    "state", state.name()
            );
            return get(UriComponentsBuilder
                    .newInstance()
                    .query("state={state}")
                    .build()
                    .toUriString(), userId, parameters);
        }
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder
                .newInstance()
                .query("state={state}")
                .query("from={from}")
                .query("size={size}")
                .build()
                .toUriString(), userId, parameters);
    }

    public ResponseEntity<Object> getBookingsOwner(BookingState state, long userId, Integer from, Integer size) {
        if (from == null && size == null) {
            Map<String, Object> parameters = Map.of(
                    "state", state.name()
            );
            return get(UriComponentsBuilder
                    .newInstance()
                    .path("/owner")
                    .query("state={state}")
                    .build()
                    .toUriString(), userId, parameters);
        }
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder
                .newInstance()
                .path("/owner")
                .query("state={state}")
                .query("from={from}")
                .query("size={size}")
                .build()
                .toUriString(), userId, parameters);
    }
}
