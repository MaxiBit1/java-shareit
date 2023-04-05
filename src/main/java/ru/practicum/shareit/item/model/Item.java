package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    private long id;
    @NotNull(message = "Error name")
    private String name;
    @NotNull(message = "Error description")
    private String description;
    @NotNull(message = "Error available")
    private Boolean available;
    private long userId;
    private ItemRequest itemRequest;
}
