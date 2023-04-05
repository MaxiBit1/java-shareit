package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Класс реализующий интерфейс ItemService
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto addItem(Item item, long userId) {
        return itemRepository.save(item, userId);
    }

    @Override
    public ItemDto updateItem(long itemId, Item item, long userId) {
        return itemRepository.update(userId, itemId, item);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        return itemRepository.getItems(userId);
    }

    @Override
    public List<ItemDto> getCurrentItems(String text) {
        return itemRepository.getSearchItem(text);
    }
}
