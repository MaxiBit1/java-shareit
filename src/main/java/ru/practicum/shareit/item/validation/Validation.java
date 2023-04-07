package ru.practicum.shareit.item.validation;

import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.model.Item;

/**
 * Класс валидации вещи
 */
public class Validation {
    /**
     * Метод валидации вещи
     *
     * @param item - вещь
     */
    public static void validation(Item item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (item.getDescription() == null) {
            throw new ValidationException();
        }
        if (item.getAvailable() == null) {
            throw new ValidationException();
        }
    }
}
