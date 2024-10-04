package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class BookingDtoCreateTest {
    @Autowired
    private JacksonTester<BookingDtoCreate> json;

    @Test
    void serializeBookingDtoCreateTest() throws Exception {
        String json = "{\"itemId\":0,\"start\":\"2016-01-06T15:22:53\",\"end\":\"2016-01-06T15:22:53\"}";
        BookingDtoCreate test = this.json.parseObject(json);
        assertThat(test.itemId(), is(0L));
        assertThat(test.start(), is(
                LocalDateTime.parse("2016-01-06T15:22:53", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        assertThat(test.end(), is(
                LocalDateTime.parse("2016-01-06T15:22:53", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}