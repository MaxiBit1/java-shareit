package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ItemRequestService {
    ItemRequestDto save(ItemRequest itemRequest, long idUser, LocalDateTime created);

    List<ItemRequestDto> getRequests(long idUser);

    ItemRequestDto getRequest(long userId, long idRequest);

    List<ItemRequestDto> getRequestsPageable(long idUser, Integer from, Integer size);
}
