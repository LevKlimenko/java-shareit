package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User save(User user);

    User update(Long id, User user);

    boolean deleteById(Long id);

    User findById(Long id);
}