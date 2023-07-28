package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemWithDateRequestTest {

    @Autowired
    private JacksonTester<ItemDtoWithRequest> json;

    @Test
    void shouldSerialiseJsonItem() throws Exception {
        ItemDtoWithRequest itemDtoWithRequest
                = new ItemDtoWithRequest(1L, "iiii", "ooooooooasdasfa", true, null);
        JsonContent<ItemDtoWithRequest> jsonContext = json.write(itemDtoWithRequest);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDtoWithRequest.getName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDtoWithRequest.getDescription());
        assertThat(jsonContext).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDtoWithRequest.getAvailable());
    }
}
