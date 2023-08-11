package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody ItemDto item,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable long itemId,
                                         @RequestBody ItemDto item,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.upgradeItem(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(value = "from", defaultValue = "0") long from,
                                           @RequestParam(value = "size", defaultValue = "0") long size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchedItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam String text,
                                                  @RequestParam(value = "from", defaultValue = "0") long from,
                                                  @RequestParam(value = "size", defaultValue = "0") long size) {
        return itemClient.getSearchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody CommentDto comment) {
        return itemClient.createComment(itemId, userId, comment);
    }
}
