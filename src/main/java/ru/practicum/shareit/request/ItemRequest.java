package ru.practicum.shareit.request;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
public class ItemRequest {
    @Id
    private long id;
    private String description;
    private long userId;
    private LocalDateTime createdDate;
}
