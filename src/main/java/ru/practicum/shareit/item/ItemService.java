package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item save(Long id, Item item);

    Item update(Long itemId, Long userId, Item item);

    boolean deleteById(Long itemId, Long userId);

    Item findById(Long itemId);

    List<Item> findByUserId(Long userId);

    List<Item> findByString(String s);
}
