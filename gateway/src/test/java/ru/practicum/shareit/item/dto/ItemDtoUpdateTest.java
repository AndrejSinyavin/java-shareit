package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class ItemDtoUpdateTest {
    @Autowired
    private JacksonTester<ItemDtoUpdate> json;

    @Test
    void serializeUserDtoUpdateTest() throws Exception {
        String json = "{\"name\":\"str\",\"description\":\"str\",\"available\":true}";
        ItemDtoUpdate test = this.json.parseObject(json);
        assertThat(test.name(), is("str"));
        assertThat(test.description(), is("str"));
        assertThat(test.available(), is(true));
    }
}