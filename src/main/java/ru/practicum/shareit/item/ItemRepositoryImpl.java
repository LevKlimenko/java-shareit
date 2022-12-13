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
import java.util.stream.Collectors;

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
        final List<Item> itemsForUser = usersItem.computeIfAbsent(userId, k -> new ArrayList<>());
        item.setId(getNewId());
        item.setOwner(userId);
        items.put(item.getId(), item);
        itemsForUser.add(item);
        return item;
    }

    @Override
    public Item update(Long itemId, Long userId, Item item) {
        usersItem.get(userId).remove(items.get(itemId));
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
        return items.values().stream()
                .filter((o) -> o.getAvailable() &&
                        (o.getName().toLowerCase().contains(s.toLowerCase()) ||
                                o.getDescription().toLowerCase().contains(s.toLowerCase()))
                )
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void checkBeforeUpdate(Long itemId, Long userId) {
        isExist(itemId);
        checkUser(userId);
        checkItemOwner(userId, itemId);
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

    private void isExist(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with ID=" + id + " not found");
        }
    }

    private long getNewId() {
        return ++itemId;
    }
}