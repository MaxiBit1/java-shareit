package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoToItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemWithDateModelTest {
    @Autowired
    private JacksonTester<ItemDtoWithDate> json;

    @Test
    void shouldSerialiseJsonItem() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("fff")
                .authorName("Max")
                .created(LocalDateTime.of(2030, 11, 2, 3, 10, 10))
                .build();
        BookingDtoToItem nextBooking = BookingDtoToItem.builder()
                .id(1L)
                .bookerId(1L)
                .build();
        BookingDtoToItem lastBooking = BookingDtoToItem.builder()
                .id(2L)
                .bookerId(2L)
                .build();
        ItemDtoWithDate itemDtoWithDate = ItemDtoWithDate.builder()
                .id(1L)
                .name("aaaaa")
                .available(true)
                .nextBooking(nextBooking)
                .lastBooking(lastBooking)
                .comments(List.of(comment))
                .description("eee")
                .build();
        JsonContent<ItemDtoWithDate> jsonContext = json.write(itemDtoWithDate);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDtoWithDate.getName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDtoWithDate.getDescription());
        assertThat(jsonContext).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDtoWithDate.isAvailable());
        assertThat(jsonContext).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo(comment.getText());
        assertThat(jsonContext).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo(comment.getAuthorName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.comments[0].created")
                .isEqualTo(comment.getCreated().toString());
        assertThat(jsonContext).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(2);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(2);
    }
}
