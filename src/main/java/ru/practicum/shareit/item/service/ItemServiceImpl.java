package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.model.NoObjectExist;
import ru.practicum.shareit.exceptions.model.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDate;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс реализующий интерфейс ItemService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Override
    public ItemDto addItem(ItemDtoWithRequest itemDtoWithRequest, long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        Item item = Item.builder()
                .id(itemDtoWithRequest.getId())
                .name(itemDtoWithRequest.getName())
                .description(itemDtoWithRequest.getDescription())
                .available(itemDtoWithRequest.getAvailable())
                .build();
        item.setUser(userOptional.get());
        if (itemDtoWithRequest.getRequestId() != null) {
            Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(itemDtoWithRequest.getRequestId());
            if (itemRequestOptional.isEmpty()) {
                throw new NoObjectExist();
            }
            item.setRequestId(itemRequestOptional.get());
        } else {
            item.setRequestId(null);
        }
        log.info("Item was created");
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(long itemId, Item item, long userId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new ValidationException();
        }
        Item oldItem = itemOptional.get();
        if (oldItem.getUser().getId() != userId) {
            throw new NoObjectExist();
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.info("Item was updated");
        itemRepository.save(oldItem);
        return ItemMapper.toItemDto(oldItem);
    }

    @Override
    public ItemDtoWithDate getItem(long itemId, long userId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        Item item = itemOptional.get();
        if (userId == item.getUser().getId()) {
            return getItemWithBooking(item);
        }
        return ItemMapper.toItemDtoWithDate(item, null, null, getComments(itemId));
    }

    @Override
    public List<ItemDtoWithDate> getItems(long userId, long from, long size) {
        if (from < 0 || size < 0) {
            throw new ValidationException();
        }
        List<ItemDtoWithDate> resultList = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        User user = userOptional.get();
        List<Item> itemsByUser = itemRepository.findAllByUser(user);
        List<Item> items;
        if (size == 0) {
            items = itemRepository.findAllByUser(user);
        } else {
            Pageable pageable = PageRequest.of((int) from, (int) size);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), itemsByUser.size());
            items = new PageImpl<>(itemsByUser.subList(start, end), pageable, itemsByUser.size()).getContent();
        }
        for (Item item : items) {
            resultList.add(getItemWithBooking(item));
        }
        return resultList
                .stream()
                .sorted(Comparator.comparing(ItemDtoWithDate::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getCurrentItems(String text, long from, long size) {
        if (from < 0 || size < 0) {
            throw new ValidationException();
        }
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<ItemDto> itemDtos = itemRepository.findItemsByText(text.toLowerCase());
        if (size == 0) {
            return itemDtos;
        } else {
            Pageable pageable = PageRequest.of((int) from, (int) size);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), itemDtos.size());
            return new PageImpl<>(itemDtos.subList(start, end), pageable, itemDtos.size()).getContent();
        }

    }

    @Override
    public CommentDto createComment(long itemId, long userId, Comment comment) {
        Optional<Booking> bookingOptional = bookingRepository.findByItemIdAndStatusBookingIs(itemId, StatusBooking.APPROVED)
                .stream()
                .filter(booking -> booking.getBooker().getId() == userId)
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .findFirst();
        if (bookingOptional.isEmpty()) {
            throw new ValidationException();
        }
        comment.setCreated(LocalDateTime.now());
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        comment.setItem(itemOptional.get());
        comment.setUser(userOptional.get());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<ItemDto> getItemsByRequestId(long requestId) {
        Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(requestId);
        if (itemRequestOptional.isEmpty()) {
            throw new NoObjectExist();
        }
        return itemRepository.findAllByRequestId(itemRequestOptional.get()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private ItemDtoWithDate getItemWithBooking(Item item) {
        Booking bookingLast = null;
        Booking bookingNext = null;
        List<Booking> bookings = bookingRepository.findByItemIdAndStatusBookingIs(item.getId(), StatusBooking.APPROVED)
                .stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());
        Optional<Booking> bookingLastOptional = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart));
        if (bookingLastOptional.isPresent()) {
            bookingLast = bookingLastOptional.get();
        }
        Optional<Booking> bookingNextOptional = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart));
        if (bookingNextOptional.isPresent()) {
            bookingNext = bookingNextOptional.get();
        }
        return ItemMapper.toItemDtoWithDate(item, bookingNext, bookingLast, getComments(item.getId()));
    }

    private List<CommentDto> getComments(long itemId) {
        return commentRepository.findByItem_id(itemId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

}
