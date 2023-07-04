package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService{

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    @Override
    public ItemRequestDto save(ItemRequest itemRequest, long idUser, LocalDateTime created) {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isEmpty()) {
            throw new RuntimeException();
        }
        return itemRequestMapper.toDto(itemRequestRepository.save(ItemRequest.builder()
                .description(itemRequest.getDescription())
                .userId(user.get())
                .createdDate(created)
                .build()));
    }

    @Override
    public List<ItemRequestDto> getRequests(long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isEmpty()) {
            throw new RuntimeException();
        }
        return itemRequestRepository.findAllByUserId(idUser, Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(long idUser, long idRequest) {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isEmpty()) {
            throw new RuntimeException();
        }
        Optional<ItemRequest> request = itemRequestRepository.findById(idRequest);
        if(request.isEmpty()) {
            throw new RuntimeException();
        }
        return itemRequestMapper.toDto(itemRequestRepository
                .findByUserIdAndId(user.get().getId(),request.get().getId()));
    }
}
