package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;

@UtilityClass
public class BookerMapper {
    public static BookingOutDto bookingOutDto(Booking booking){
        return new BookingOutDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                null,
                null
        );
    }

    public static Booking toBooking(BookingInDto bookingInDto){
        Booking booking = new Booking();
        booking.setItem(bookingInDto.getItemId());
        booking.setStart(bookingInDto.getStart());
        booking.setEnd(bookingInDto.getEnd());
        return booking;
    }
}
