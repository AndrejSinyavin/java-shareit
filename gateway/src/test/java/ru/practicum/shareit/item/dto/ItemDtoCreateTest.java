package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class ItemDtoCreateTest {
    @Autowired
    private JacksonTester<ItemDtoCreate> json;

    @Test
    void serializeItemDtoCreateTest() throws Exception {
        String json = "{\"name\":\"str\",\"description\":\"str\",\"available\":true,\"requestId\":0}";
        ItemDtoCreate test = this.json.parseObject(json);
        assertThat(test.name(), is("str"));
        assertThat(test.description(), is("str"));
        assertThat(test.available(), is(true));
        assertThat(test.requestId(), is(0L));
    }
}