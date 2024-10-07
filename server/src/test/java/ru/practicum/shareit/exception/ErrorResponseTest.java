package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void getError() {
        var errorResponse = new ErrorResponse("1", "1");
        assertEquals("1", errorResponse.getError().get("1"));
    }
}