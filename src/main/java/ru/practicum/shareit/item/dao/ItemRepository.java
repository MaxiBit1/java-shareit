package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфес для работы с бд вещей
 */
public interface ItemRepository {
    /**
     * Метод для сохрранения вещи в бд
     *
     * @param item   - вещь
     * @param userId - айди юзера
     * @return - сохраненная вещь
     */
    ItemDto save(Item item, long userId);

    /**
     * Метод для обновления вещи
     *
     * @param userId - айди юзера
     * @param itemId - айди вещи
     * @param item   - вещь
     * @return - обновленная вещь
     */
    ItemDto update(long userId, long itemId, Item item);

    /**
     * Метод для получения вещи
     *
     * @param itemId - айди вещи
     * @return - найдена вещь
     */
    ItemDto getItem(long itemId);

    /**
     * Метод получения списка вещей для определенного пользователя
     *
     * @param userId - айди пользователя
     * @return - список вещей
     */
    List<ItemDto> getItems(long userId);

    /**
     * Метод для поиска вещей по тексту
     *
     * @param text - текст
     * @return - список найденных вещей
     */
    List<ItemDto> getSearchItem(String text);
}
