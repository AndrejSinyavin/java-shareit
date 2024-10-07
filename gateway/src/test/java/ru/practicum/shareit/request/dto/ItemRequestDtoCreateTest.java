package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class ItemRequestDtoCreateTest {
    @Autowired
    private JacksonTester<ItemRequestDtoCreate> json;

    @Test
    void serializeItemRequestDtoCreateTest() throws Exception {
        String json = "{\"description\":\"str\"}";
        ItemRequestDtoCreate test = this.json.parseObject(json);
        assertThat(test.description(), is("str"));
    }
}