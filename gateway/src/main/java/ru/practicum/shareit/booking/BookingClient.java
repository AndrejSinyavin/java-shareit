package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.client.BaseClient;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingClient extends BaseClient {
    static String API_PREFIX = "/bookings";
    static String LOG_MESSAGE = "Validated: ok  Forwarded to server";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public Object add(long bookerId, BookingDtoCreate requestDto) {
        log.info(LOG_MESSAGE);
        return post("", bookerId, requestDto);
    }

    public Object approve(long ownerId, long bookingId, boolean approved) {
        log.info(LOG_MESSAGE);
        return patch("/" + bookingId + "?approved=" + approved, ownerId);
    }

    public Object get(long userId, long bookingId) {
        log.info(LOG_MESSAGE);
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByUser(long userId, String state) {
        log.info(LOG_MESSAGE);
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getBookingsForOwnerItems(long ownerId, String state) {
        log.info(LOG_MESSAGE);
        return get("/owner?state=" + state, ownerId);
    }

}
