package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> usersItem = itemService.findByUserId(userId);
        log.info("The user's items have been received for UserID={}", userId);
        return usersItem;
    }

    @PostMapping
    public Item save(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody Item item) {
        Item addedItem = itemService.save(userId, item);
        log.info("The user's item have been add for UserID={}, ItemID={}", userId, addedItem.getId());
        return addedItem;
    }

    @PatchMapping("/{itemId}")
    public Item update(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody Item item) {
        Item upItem = itemService.update(itemId, userId, item);
        log.info("The user's item have been update for UserID={}, ItemID={}", userId, upItem.getId());
        return upItem;
    }

    @DeleteMapping("/{itemId}")
    public boolean delete(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        boolean del = itemService.deleteById(itemId, userId);
        log.info("The user's item have been deleted for UserID={}, ItemID={}", userId, itemId);
        return del;
    }

    @GetMapping("/{itemId}")
    public Item findById(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findById(itemId);
        log.info("The item was found, ItemID={}", item.getId());
        return item;
    }

    @GetMapping("/search")
    public List<Item> findByRequest(@RequestParam String text) {
        List<Item> items = itemService.findByString(text);
        log.info("Items were found on request '{}'", text);
        return items;
    }
}