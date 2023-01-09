package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.enumBooking.Status;

import java.util.List;

public interface BookingService {
    BookingOutDto save(Long userId, BookingInDto bookingInDto);
    BookingOutDto approve(Long userId, Long bookingId, Boolean approved);
    BookingOutDto findById(Long userId,Long bookingId);
    List<BookingOutDto> findAll(Long userId, Status state);
    List<BookingOutDto> findAllOwner(Long userId, String state);
}
