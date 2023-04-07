package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.model.NoUserExist;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validation.Validation;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс реализующий интерфейс ItemRepository
 */
@Component
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    private final ItemMapper itemMapper;
    private final UserRepository userRepository;

    private final Map<Long, Item> storageItem = new HashMap<>();

    @Autowired
    public ItemRepositoryImpl(ItemMapper itemMapper, UserRepository userRepository) {
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }


    @Override
    public ItemDto save(Item item, long userId) {
        if (!checkUserId(userId)) {
            throw new NoUserExist();
        }
        Validation.validation(item);
        item.setId(getId());
        item.setUserId(userId);
        storageItem.put(item.getId(), item);
        log.info("Вещь создана");
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, Item item) {
        Item oldItem = storageItem.get(itemId);
        if (oldItem.getUserId() != userId) {
            throw new NoUserExist();
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.info("Вещи обновлена");
        return itemMapper.toItemDto(oldItem);
    }

    @Override
    public ItemDto getItem(long itemId) {
        log.info("Вещь получена");
        return itemMapper.toItemDto(storageItem.get(itemId));
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        log.info("Вещи получены");
        return storageItem.values()
                .stream()
                .filter(item -> item.getUserId() == userId)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearchItem(String text) {
        log.info("Вещь найдена");
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return storageItem.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> this.searchItem(text, item.getName(), item.getDescription()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /**
     * получения айди для вещи
     *
     * @return - айди
     */
    private long getId() {
        long lastId = storageItem.values()
                .stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }

    /**
     * проверка на наличии пользователя
     *
     * @param userId - айди юзера
     * @return - результат проверки
     */
    private boolean checkUserId(long userId) {
        return userRepository.getStorage().containsKey(userId);
    }

    /**
     * Метод получения сравнения в названии или в описании вещи
     *
     * @param text        - текст
     * @param name        - название вещи
     * @param description - описание вещи
     * @return - результат проверки
     */
    private boolean searchItem(String text, String name, String description) {
        String lowerCaseText = text.toLowerCase();
        return name.toLowerCase().contains(lowerCaseText)
                || description.toLowerCase().contains(lowerCaseText);
    }

}
