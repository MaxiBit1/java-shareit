package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Инитерфейс сервиса для вещей
 */
public interface ItemService {
    /**
     * Метод добавления вещи
     *
     * @param item   - вещь
     * @param userId - айди юзера
     * @return - добавленная вещь
     */
    ItemDto addItem(Item item, long userId);

    /**
     * Обновление вещи
     *
     * @param itemId - айди вещи
     * @param item   - вещь
     * @param userId - айди юзера
     * @return - обновленная вещь
     */
    ItemDto updateItem(long itemId, Item item, long userId);

    /**
     * получение вези по айди
     *
     * @param itemId - айди вещи
     * @return - получение вещи
     */
    ItemDto getItem(long itemId);

    /**
     * Метод получения списка вещей
     *
     * @param userId - айди юзера
     * @return - список вещей
     */
    List<ItemDto> getItems(long userId);

    /**
     * Получение определенной вещи
     *
     * @param text - текст
     * @return - получение вещи
     */
    List<ItemDto> getCurrentItems(String text);
}
