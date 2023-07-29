package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentModelTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void shouldSerialiseJsonItem() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("fff")
                .authorName("Max")
                .created(LocalDateTime.of(2030, 11, 2, 3, 10, 10))
                .build();
        JsonContent<CommentDto> jsonContext = json.write(comment);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.text")
                .isEqualTo(comment.getText());
        assertThat(jsonContext).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(comment.getAuthorName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.created")
                .isEqualTo(comment.getCreated().toString());
    }
}
