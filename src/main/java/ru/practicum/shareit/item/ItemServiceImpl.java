package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto save(Long userId, ItemDto itemDto) {
        User user = userRepository.get(userId);
        if (Objects.isNull(itemDto.getAvailable())) {
            throw new ConflictException("Available can't be NULL");
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        userRepository.get(userId);
        Item item = itemRepository.get(itemId);
        if (!userId.equals(item.getOwner().getId())) {
            throw new ForbiddenException("User with ID=" + userId + " not owner for item with ID=" + itemId);
        }
        item = checkUpdate(itemId, ItemMapper.toItem(itemDto));
        itemRepository.save(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void deleteById(Long itemId, Long userId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item item = itemRepository.get(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findByString(String s) {
        return itemRepository.findByString(s).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findByUserId(Long id) {
        return itemRepository.findAllByOwnerIdOrderById(id).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private Item checkUpdate(Long itemId, Item item) {
        Item findItem = itemRepository.get(itemId);
        if (item.getName() != null && !item.getName().isBlank()) {
            findItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            findItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            findItem.setAvailable(item.getAvailable());
        }
        return findItem;
    }

    private Booking findLastBooking(Long itemId){
        return bookingRepository.findLastBookingByItemId(itemId,LocalDateTime.now());
    }

    private Booking findNextBooking(Long itemId){
        return bookingRepository.findNextBookingByItemId(itemId, LocalDateTime.now());
    }

    private boolean isAuthorUsedItem(Long userId, Long itemId){
        int count = bookingRepository.countCompletedBooking(userId,itemId, LocalDateTime.now());
        return count>0;
    }

    private List<Comment> findComment(long itemId){
        return commentRepository.findAllByItemOrderByCreatedDesc(itemId);
    }

}