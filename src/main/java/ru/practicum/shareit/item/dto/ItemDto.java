package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Long requestId;
}
