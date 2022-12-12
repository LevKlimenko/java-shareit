package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAll() {
        List<UserDto> listDto = new ArrayList<>();
        for (User user : repository.findAll()) {
            listDto.add(UserMapper.toUserDto(user));
        }
        return listDto;
    }

    @Override
    public UserDto save(UserDto userDto) {
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
        UserDto findUser = findById(id);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(findUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(findUser.getEmail());
        }
        return user;
    }
}