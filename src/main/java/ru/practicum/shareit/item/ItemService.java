package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto save(Long id, ItemDto item);

    ItemDto update(Long itemId, Long userId, ItemDto item);

    boolean deleteById(Long itemId, Long userId);

    ItemDto findById(Long itemId);

    List<ItemDto> findByUserId(Long userId);

    List<ItemDto> findByString(String s);
}