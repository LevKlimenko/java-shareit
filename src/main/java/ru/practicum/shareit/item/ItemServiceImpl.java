package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

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
        List<ItemDto> listDto = new ArrayList<>();
        for (Item item :
                itemRepository.findByString(s)) {
            listDto.add(ItemMapper.toItemDto(item));
        }
        return listDto;
    }

    @Override
    public List<ItemDto> findByUserId(Long id) {
        List<ItemDto> listDto = new ArrayList<>();
        for (Item item :
                itemRepository.findByUserId(id)) {
            listDto.add(ItemMapper.toItemDto(item));
        }
        return listDto;
    }

    private ItemDto checkUpdate(Long itemId, ItemDto item) {
        ItemDto findItem = findById(itemId);
        if (item.getName() == null || item.getName().isBlank()) {
            item.setName(findItem.getName());
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription(findItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(true);
        }
        return item;
    }
}