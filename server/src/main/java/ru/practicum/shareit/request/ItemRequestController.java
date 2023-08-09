package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto saveItemRequest(@RequestBody ItemRequest itemRequest,
                                          @RequestHeader("X-Sharer-User-Id") long idUser) {
        log.info("Сохранен запрос юзера: " + idUser);
        return itemRequestService.save(itemRequest, idUser, LocalDateTime.now());
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") long idUser) {
        log.info("Получены запросы юзера: " + idUser);
        return itemRequestService.getRequests(idUser);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") long idUser, @PathVariable long requestId) {
        log.info("Получен запрос юзера: " + idUser + "с id запроса: " + requestId);
        return itemRequestService.getRequest(idUser, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestsPagination(@RequestHeader("X-Sharer-User-Id") long idUser,
                                                          @RequestParam(value = "from", required = false) Integer from,
                                                          @RequestParam(value = "size", required = false) Integer size) {
        log.info("Получена пагинация для запросов from: " + from + "с size: " + size);
        return itemRequestService.getRequestsPageable(idUser, from, size);
    }
}
