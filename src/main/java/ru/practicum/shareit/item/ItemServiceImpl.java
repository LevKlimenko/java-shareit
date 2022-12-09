package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item save(Long userId, Item item) {
        return itemRepository.save(userId, item);
    }

    @Override
    public Item update(Long itemId, Long userId, Item item) {
        return itemRepository.update(itemId, userId, item);
    }

    @Override
    public boolean deleteById(Long itemId, Long userId) {
        return itemRepository.deleteById(itemId, userId);
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public List<Item> findByString(String s) {
        return itemRepository.findByString(s);
    }
}