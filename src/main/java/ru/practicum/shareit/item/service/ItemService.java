package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс сервиса для вещей
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
    ItemDtoWithDate getItem(long itemId, long userId);

    /**
     * Метод получения списка вещей
     *
     * @param userId - айди юзера
     * @return - список вещей
     */
    List<ItemDtoWithDate> getItems(long userId);

    /**
     * Получение определенной вещи
     *
     * @param text - текст
     * @return - получение вещи
     */
    List<ItemDto> getCurrentItems(String text);

    /**
     * Создание комментария
     * @param itemId - вещь
     * @param userId - юзер
     * @param comment - комментарий
     * @return - сохраненный комментарий
     */
    CommentDto createComment(long itemId, long userId, Comment comment);

    List<ItemDto> getItemsByRequestId(long requestId);
}
