package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * Интерфейс для работы с бд вещей
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * Нахождение всех вещей по юзеру
     * @param user - юзер
     * @return - список вещей
     */
    List<Item> findAllByUser(User user);

    /**
     * нахождение вещи по тексту
     * @param text - текст
     * @return - список вещей
     */
    @Query("select new ru.practicum.shareit.item.dto.ItemDto(it.id, it.name, it.description, it.available) " +
            "from Item as it " +
            "where lower( it.name) like lower(concat('%', ?1,'%') )" +
            "or lower(it.description) like lower(concat('%', ?1,'%'))" +
            "and it.available = true ")
    List<ItemDto> findItemsByText(String text);
}
