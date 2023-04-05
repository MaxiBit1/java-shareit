package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс создания объекта DTO
 */
@Component
public class ItemMapping {

    /**
     * Метод создания объекта DTO
     * @param item - вещь
     * @return - itemDto
     */
    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
