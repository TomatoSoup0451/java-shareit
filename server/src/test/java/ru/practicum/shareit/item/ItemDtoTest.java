package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void shouldCreateItemDto() throws IOException {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .owner(2)
                .name("name")
                .available(true)
                .description("description")
                .build();
        JsonContent<ItemDto> result = json.write(itemDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

}