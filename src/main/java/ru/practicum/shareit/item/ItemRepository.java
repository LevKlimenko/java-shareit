package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository {
    Collection<Item> findAll();

    Item save(Long userId, Item item);

    Item update(Long itemId, Long userId, Item item);

    void checkBeforeUpdate(Long itemId, Long userId);

    boolean deleteById(Long itemId, Long userId);

    Item findById(Long itemId);

    List<Item> findByUserId(Long userId);

    List<Item> findByString(String s);

}