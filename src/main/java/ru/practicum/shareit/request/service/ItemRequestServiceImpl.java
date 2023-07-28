package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.model.NoObjectExist;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto save(ItemRequest itemRequest, long idUser, LocalDateTime created) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new RuntimeException();
        }
        return itemRequestMapper.toDto(itemRequestRepository.save(ItemRequest.builder()
                .description(itemRequest.getDescription())
                .user(user.get())
                .created(created)
                .build()));
    }

    @Override
    public List<ItemRequestDto> getRequests(long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new RuntimeException();
        }
        return checkList(user.get());
    }


    @Override
    public ItemRequestDto getRequest(long idUser, long idRequest) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new NoObjectExist();
        }
        Optional<ItemRequest> request = itemRequestRepository.findById(idRequest);
        if (request.isEmpty()) {
            throw new NoObjectExist();
        }
        if (checkList(user.get()).isEmpty()) {
            return itemRequestMapper.toDto(request.get());
        }
        return itemRequestMapper.toDto(itemRequestRepository
                .findByIdAndUser(idRequest, user.get()));
    }

    @Override
    public List<ItemRequestDto> getRequestsPageable(long idUser, Integer from, Integer size) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new RuntimeException();
        }
        if (size != null) {
            if (from < 0 || size <= 0) {
                throw new ValidationException();
            }
            try {
                List<ItemRequest> itemRequests = itemRequestRepository
                        .findAll(PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created")))
                        .getContent();
                List<ItemRequestDto> userListRequests = checkList(user.get());
                if (userListRequests.isEmpty()) {
                    return itemRequests
                            .stream()
                            .map(itemRequestMapper::toDto)
                            .collect(Collectors.toList());
                } else {
                    return itemRequests
                            .stream()
                            .map(itemRequestMapper::toDto)
                            .filter(userListRequests::contains)
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return checkList(user.get());
    }


    private List<ItemRequestDto> checkList(User user) {
        List<ItemRequest> itemRequests
                = itemRequestRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "created"));
        if (itemRequests.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRequests
                .stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());

    }


}
