package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemModelTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void shouldSerialiseJsonItem() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .available(true)
                .name("aaaa")
                .description("fffff")
                .build();
        JsonContent<ItemDto> jsonContext = json.write(itemDto);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(jsonContext).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.isAvailable());
    }
}
