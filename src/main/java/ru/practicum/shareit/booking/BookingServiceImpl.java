package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enumBooking.State;
import ru.practicum.shareit.booking.enumBooking.Status;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto save(Long userId, BookingIncomingDto dto) {
        User booker = userRepository.get(userId);
        Item item = itemRepository.get(dto.getItemId());
        if (userId.equals(item.getOwner().getId())) {
            throw new BadRequestException("The owner of the Item cannot booking his Item");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item ID=" + item.getId() + "not available now for booking");
        }
        Booking newBooking = BookingMapper.toBooking(dto,item,booker);
        Booking createBooking = bookingRepository.save(newBooking);
        return BookingMapper.toBookingDto(createBooking);
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean approved) {
        userRepository.get(userId);
        Booking booking = bookingRepository.get(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
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
        Booking updateBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updateBooking);
    }

    @Override
    public BookingDto findById(Long userId, Long bookingId) {
        User user = userRepository.get(userId);
        Booking booking = bookingRepository.get(bookingId);
        if (!booking.getBooker().equals(user) && !booking.getItem().getOwner().equals(user)){
            throw new NotFoundException("It's not possible to get booking id="+bookingId+ " for user id="+userId);
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findAllForBooker(Long userId, State state) {
        userRepository.get(userId);
       LocalDateTime now = LocalDateTime.now();
       Collection<Booking> result;
       switch (state){
           case ALL:
               result=bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
               break;
           case WAITING:
               result=bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(userId,Status.WAITING);
               break;
           case REJECTED:
               result=bookingRepository.findAllByBookerIdAndStatusEqualsOrderByStartDesc(userId, Status.REJECTED);
               break;
           case CURRENT:
               result=bookingRepository.findAllByBookerCurrent(userId,now);
               break;
           case PAST:
               result=bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId,now);
               break;
           case FUTURE:
               result=bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,now);
               break;
           default:
               throw new BadRequestException("Unknown state: " + state);
       }
       return Optional.ofNullable(result)
               .orElseGet(Collections::emptyList)
               .stream()
               .map(BookingMapper::toBookingDto)
               .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllForOwner(Long userId, State state) {
        return null;
    }


}
