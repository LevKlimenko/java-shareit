package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
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
        repository.checkAlreadyExistEmail(UserMapper.toUser(userDto));
        User user = repository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        UserDto user = checkUpdate(id, userDto);
        User upUser = repository.update(id, UserMapper.toUser(user));
        return UserMapper.toUserDto(upUser);
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public UserDto findById(Long id) {
        return UserMapper.toUserDto(repository.findById(id));
    }

    private UserDto checkUpdate(Long id, UserDto user) {
        User findUser = repository.findById(id);
        if (user.getName() != null && !user.getName().isBlank()) {
            findUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            repository.checkAlreadyExistEmail(UserMapper.toUser(user));
            repository.removeOldEmail(findUser.getEmail());
            findUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(findUser);
    }
}