package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User save(User user);

    User update(Long id, User user);

    boolean deleteById(Long id);

    User findById(Long id);

    void checkAlreadyExistEmail(User user);

    void removeOldEmail(String oldEmail);
}