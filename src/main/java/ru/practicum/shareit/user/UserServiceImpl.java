package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = repository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        UserDto user = checkUpdate(id, userDto);
        User upUser = repository.save(UserMapper.toUser(user));
        return UserMapper.toUserDto(upUser);
    }

    @Override
    public boolean deleteById(Long id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.toUserDto(repository.get(id));
    }

    private UserDto checkUpdate(Long id, UserDto user) {
        User findUser = repository.get(id);
        if (user.getName() != null && !user.getName().isBlank()) {
            findUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank() &&
                !Objects.equals(user.getEmail(), findUser.getEmail())) {
            findUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(findUser);
    }
}