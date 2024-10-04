package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class UserDtoUpdateTest {
    @Autowired
    private JacksonTester<UserDtoUpdate> json;

    @Test
    void serializeUserDtoUpdateTest() throws Exception {
        String json = "{\"name\":\"str\",\"email\":\"str\"}";
        UserDtoUpdate test = this.json.parseObject(json);
        assertThat(test.email(), is("str"));
        assertThat(test.name(), is("str"));
    }
}