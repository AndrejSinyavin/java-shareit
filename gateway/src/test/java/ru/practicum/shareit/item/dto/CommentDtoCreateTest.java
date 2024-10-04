package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
class CommentDtoCreateTest {
    @Autowired
    private JacksonTester<CommentDtoCreate> json;

    @Test
    void serializeCommentDtoCreateTest() throws Exception {
        String json = "{\"text\":\"str\"}";
        CommentDtoCreate test = this.json.parseObject(json);
        assertThat(test.text(), is("str"));
    }

}