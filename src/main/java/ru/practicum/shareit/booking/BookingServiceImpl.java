package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{
    @Override
    public BookingOutDto save(Long userID, BookingInDto bookingInDto) {
        return null;
    }

    @Override
    public BookingOutDto approve(Long userId, Long bookingId, Boolean approved) {
        return null;
    }

    @Override
    public BookingOutDto findById(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingOutDto> findAll(Long userId, String state) {
        return null;
    }

    @Override
    public List<BookingOutDto> findAllOwner(Long userId, String state) {
        return null;
    }
}
