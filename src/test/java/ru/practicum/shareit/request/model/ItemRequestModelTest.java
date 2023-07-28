package ru.practicum.shareit.request.model;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestModelTest {

    private JacksonTester<ItemRequestDto> json;

    @Test
    void shouldSerialiseJsonItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("aaaaa")
                .created(LocalDateTime.now())
                .build();
        JsonContent<ItemRequestDto> jsonContext = json.write(itemRequestDto);
        assertThat(jsonContext).extractingJsonPathArrayValue("$.id")
                .isEqualTo(itemRequestDto.getId());
        assertThat(jsonContext).extractingJsonPathArrayValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(jsonContext).extractingJsonPathArrayValue("$.created")
                .isEqualTo(itemRequestDto.getCreated());
    }
}
