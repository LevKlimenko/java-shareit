package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        userService.findById(userId);
        Item item = itemRepository.save(userId, ItemMapper.toItem(itemDto));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        userService.findById(userId);
        itemRepository.checkBeforeUpdate(itemId, userId);
        ItemDto item = checkUpdate(itemId, itemDto);
        Item upItem = itemRepository.update(itemId, userId, ItemMapper.toItem(item));
        return ItemMapper.toItemDto(upItem);
    }

    @Override
    public boolean deleteById(Long itemId, Long userId) {
        return itemRepository.deleteById(itemId, userId);
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findByString(String s) {
        return itemRepository.findByString(s).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findByUserId(Long id) {
        return itemRepository.findByUserId(id).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private ItemDto checkUpdate(Long itemId, ItemDto item) {
        Item findItem = itemRepository.findById(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            findItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            findItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            findItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(findItem);
    }
}