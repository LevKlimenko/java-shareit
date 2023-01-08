package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto save(Long userID, BookingInDto bookingInDto);
    BookingOutDto approve(Long userId, Long bookingId, Boolean approved);
    BookingOutDto findById(Long userId,Long bookingId);
    List<BookingOutDto> findAll(Long userId,String state);
    List<BookingOutDto> findAllOwner(Long userId, String state);
}
