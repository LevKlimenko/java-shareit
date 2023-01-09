package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.enumBooking.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingOutDto save(Long userId, BookingInDto bookingInDto) {
        User user = userRepository.get(userId);
        Item item = itemRepository.get(bookingInDto.getItemId());
        if (userId.equals(item.getOwner())) {
            throw new ConflictException("The owner of the Item cannot book his Item");
        }
        if (!item.getAvailable()) {
            throw new ConflictException("Item ID=" + item.getId() + "not available now for booking");
        }
        Booking booking = bookingRepository.save(bookingMapper.toBooking(bookingInDto, item, user));
        return bookingMapper.bookingToBookingOutDto(booking);
    }

    @Override
    public BookingOutDto approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking ID=" + bookingId + " not found"));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException("Item ID=" + booking.getItem().getId() + "doesn't belong to the user ID=" + userId);
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BadRequestException("Booking id=" + bookingId + " is already approved");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.bookingToBookingOutDto(booking);
    }

    @Override
    public BookingOutDto findById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking id=" + bookingId + " not found"));
        if (booking.getBooker().getId().equals(userId)|| booking.getItem().getOwner().equals(userId)){
            return bookingMapper.bookingToBookingOutDto(booking);
        }
        throw new NotFoundException("Booking id="+bookingId+ " doesn't belong to User id="+userId);
    }

    @Override
    public List<BookingOutDto> findAll(Long userId, Status state) {
        userRepository.get(userId);
        BookingStatus status = isExistStatus(state);
        if (status.equals(BookingStatus.PAST)){
            return bookingRepository.findAllByBookerIdAndStatusAndEndBefore(userId,
                    BookingStatus.APPROVED,
                    LocalDateTime.now(),
                    Sort.by(Sort.Direction.DESC,"end")).stream()
                    .map(bookingMapper::bookingToBookingOutDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<BookingOutDto> findAllOwner(Long userId, String state) {
        return null;
    }

private BookingStatus isExistStatus(Status status){
        switch(status){
            case CURRENT:
                return BookingStatus.REJECTED;
            case APPROVED:
                return BookingStatus.APPROVED;
            case WAITING:
                return BookingStatus.WAITING;
            default:throw new  BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
}
}
