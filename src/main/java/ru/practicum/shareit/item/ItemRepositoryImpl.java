package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> usersItem = new HashMap<>();
    private long itemId;

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Item save(Long userId, Item item) {
        checkUser(userId);
        List<Item> itemsForUser = usersItem.get(userId);
        if (itemsForUser == null) {
            itemsForUser = new ArrayList<>();
        }
        checkBody(item);
        item.setId(getNewId());
        item.setOwner(userId);
        items.put(item.getId(), item);
        itemsForUser.add(item);
        usersItem.put(userId, itemsForUser);
        return item;
    }

    @Override
    public Item update(Long itemId, Long userId, Item item) {
        isExist(itemId);
        checkUser(userId);
        checkItemOwner(userId, itemId);
        usersItem.get(userId).remove(items.get(itemId));
        checkUpdate(itemId, item);
        item.setOwner(userId);
        items.put(itemId, item);
        usersItem.get(userId).add(item);
        return item;
    }

    @Override
    public boolean deleteById(Long itemId, Long userId) {
        Item delItem = items.get(itemId);
        isExist(itemId);
        checkItemOwner(userId, itemId);
        usersItem.get(userId).remove(delItem);
        items.remove(itemId);
        return true;
    }

    @Override
    public Item findById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Item with ID=" + itemId + " not found");
        }
        return item;
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        return usersItem.get(userId);
    }

    @Override
    public List<Item> findByString(String s) {
        List<Item> itemsSearch = new ArrayList<>();
        if (s.isBlank())
            return itemsSearch;
        for (Long id : items.keySet()) {
            if (items.get(id).getAvailable()) {
                if (items.get(id).getName().toLowerCase().contains(s.toLowerCase())) {
                    itemsSearch.add(items.get(id));
                    continue;
                }
                if (items.get(id).getDescription().toLowerCase().contains(s.toLowerCase())) {
                    itemsSearch.add(items.get(id));
                }
            }
        }
        return itemsSearch;
    }

    private void checkUser(Long id) {
        if (id == null) {
            throw new BadRequestException("User's id can't be NULL");
        }
        if (userRepository.findById(id) == null) {
            throw new NotFoundException("User with ID=" + id + " not found");
        }
    }

    private void checkItemOwner(Long userId, Long itemId) {
        if (!items.get(itemId).getOwner().equals(userId)) {
            throw new NotFoundException("You are not owner for this item");
        }
    }

    private void checkBody(Item item) {
        if (item.getName().isBlank()) {
            throw new BadRequestException("Item's name can't be empty");
        }
        if (item.getDescription() == null) {
            throw new BadRequestException("Item's description can't be empty");
        }
        if (item.getAvailable() == null) {
            throw new BadRequestException("Item's availability can't be empty");
        }
    }

    private void checkUpdate(Long itemId, Item item) {
        if (item.getName() == null) {
            item.setName(findById(itemId).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(findById(itemId).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(true);
        }
        item.setId(itemId);
    }

    private void isExist(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with ID=" + id + " not found");
        }
    }

    private long getNewId() {
        return ++itemId;
    }
}