package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> saveItemRequest(@Valid @RequestBody ItemRequestDto itemRequest,
                                                  @RequestHeader("X-Sharer-User-Id") long idUser) {
        log.info("Save request {} with userId={}", itemRequest, idUser);
        return itemRequestClient.createItemRequest(idUser, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") long idUser) {
        log.info("Get item requests with idUser={}", idUser);
        return itemRequestClient.getItemRequests(idUser);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long idUser, @PathVariable long requestId) {
        log.info("Get item request id={} with idUser={}", requestId, idUser);
        return itemRequestClient.getItemRequest(idUser, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsPagination(@RequestHeader("X-Sharer-User-Id") long idUser,
                                                            @RequestParam(value = "from", required = false) Integer from,
                                                            @RequestParam(value = "size", required = false) Integer size) {
        log.info("Get pagination item requests from={}, size={} with idUser={}", from, size, idUser);
        return itemRequestClient.getItemRequestAll(idUser, from, size);
    }
}
