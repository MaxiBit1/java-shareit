package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {


    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto add(@Valid @RequestBody ItemDtoWithRequest item,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestBody Item item,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithDate getItem(@PathVariable long itemId,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoWithDate> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(value = "from", defaultValue = "0") long from,
                                          @RequestParam(value = "size", defaultValue = "0") long size) {
        return itemService.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchedItem(@RequestParam String text,
                                         @RequestParam(value = "from", defaultValue = "0") long from,
                                         @RequestParam(value = "size", defaultValue = "0") long size) {
        return itemService.getCurrentItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody Comment comment) {
        return itemService.createComment(itemId, userId, comment);
    }


}
