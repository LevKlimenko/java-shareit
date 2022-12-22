package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    boolean deleteById(Long id);

    UserDto findById(Long id);
}