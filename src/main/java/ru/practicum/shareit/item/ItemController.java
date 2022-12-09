package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> usersItem = itemService.findByUserId(userId);
        log.info("The user's items have been received for UserID={}", userId);
        return usersItem;
    }

    @PostMapping
    public ItemDto save(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto item) {
        ItemDto addedItem = itemService.save(userId, item);
        log.info("The user's item have been add for UserID={}, ItemID={}", userId, addedItem.getId());
        return addedItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto item) {
        ItemDto upItem = itemService.update(itemId, userId, item);
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
    public ItemDto findById(@PathVariable("itemId") Long itemId) {
        ItemDto item = itemService.findById(itemId);
        log.info("The item was found, ItemID={}", item.getId());
        return item;
    }

    @GetMapping("/search")
    public List<ItemDto> findByRequest(@RequestParam String text) {
        List<ItemDto> items = itemService.findByString(text);
        log.info("Items were found on request '{}'", text);
        return items;
    }
}