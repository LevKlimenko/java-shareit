package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "status", defaultValue = "WAITING")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(BookingInDto bookingInDto, Item item, User booker);

    BookingOutDto bookingToBookingOutDto(Booking booking);
}
