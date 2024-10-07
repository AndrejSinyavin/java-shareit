package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class UserDtoCreateTest {
    @Autowired
    private JacksonTester<UserDtoCreate> json;

    @Test
    void serializeUserDtoCreateTest() throws Exception {
        String userDtoJson = "{\"name\":\"str\",\"email\":\"str\"}";
        UserDtoCreate test = json.parseObject(userDtoJson);
        assertThat(test.email(), is("str"));
        assertThat(test.name(), is("str"));
    }
}