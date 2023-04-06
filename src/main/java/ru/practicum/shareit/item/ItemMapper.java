package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

/**
 * Класс создания объекта DTO
 */
@Component
public class ItemMapper {

    /**
     * Метод создания объекта DTO
     *
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

    /**
     * Метод преобразованият из объекта DTO в объект item
     *
     * @param itemDto - объект DTO
     * @param user    - юзер
     * @return - объект item
     */
    public Item toNewItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .userId(user.getId())
                .build();
    }

}
