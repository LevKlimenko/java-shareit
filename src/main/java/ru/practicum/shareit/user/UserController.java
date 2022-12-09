package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.getAll();
        log.info("The list of all users has been received");
        return users;
    }

    @PostMapping
    public User save(@Validated(Create.class) @RequestBody User user) {
        User addedUser = userService.save(user);
        log.info("The user have been add, UserID={}", addedUser.getId());
        return addedUser;
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable("userId") Long id, @Validated(Update.class) @RequestBody User user) {
        User upUser = userService.update(id, user);
        log.info("The user have been update, UserID={}", upUser.getId());
        return upUser;
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable("userId") Long id) {
        User user = userService.findById(id);
        log.info("The user have been founded, UserID={}", user.getId());
        return userService.findById(id);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteById(@PathVariable("userId") Long id) {
        boolean del = userService.deleteById(id);
        log.info("The user have been deleted, UserID={}", id);
        return del;
    }
}