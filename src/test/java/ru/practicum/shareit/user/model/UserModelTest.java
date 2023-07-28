package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class UserModelTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;


    @Test
    void shouldSerialiseJsonUser() throws Exception {
        UserDto userDto = new UserDto(1L, "aaa", "ffff@mail.com");
        JsonContent<UserDto> jsonContext = jacksonTester.write(userDto);
        assertThat(jsonContext).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContext).extractingJsonPathStringValue("$.name")
                .isEqualTo(userDto.getName());
        assertThat(jsonContext).extractingJsonPathStringValue("$.email")
                .isEqualTo(userDto.getEmail());
    }
}
