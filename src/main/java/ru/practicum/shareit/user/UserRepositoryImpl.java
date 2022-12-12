package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmailInBase = new HashSet<>();
    private long id;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        checkAlreadyExistEmail(user);
        user.setId(getNewId());
        users.put(id, user);
        usersEmailInBase.add(user.getEmail());
        return user;
    }

    @Override
    public User update(Long id, User user) {
        isExist(id);
        usersEmailInBase.remove(findById(id).getEmail());
        checkAlreadyExistEmail(user);
        user.setId(id);
        users.put(id, user);
        usersEmailInBase.add(user.getEmail());
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        usersEmailInBase.remove(users.get(id).getEmail());
        users.remove(id);
        return true;
    }

    @Override
    public User findById(Long id) {
        isExist(id);
        return users.get(id);
    }

    private long getNewId() {
        return ++id;
    }

    private void checkAlreadyExistEmail(User user) {
        if (usersEmailInBase.contains(user.getEmail())) {
            throw new ConflictException("User with e-mail " + user.getEmail() +
                    " already exist");
        }
    }

    private void isExist(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with ID=" + id + " not found");
        }
    }
}