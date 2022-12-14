package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userService.getAll();
        log.info("The list of all users has been received");
        return users;
    }

    @PostMapping
    public UserDto save(@Validated(Create.class) @RequestBody UserDto user) {
        UserDto addedUser = userService.save(user);
        log.info("The user have been add, UserID={}", addedUser.getId());
        return addedUser;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long id, @Validated(Update.class) @RequestBody UserDto user) {
        UserDto upUser = userService.update(id, user);
        log.info("The user have been update, UserID={}", upUser.getId());
        return upUser;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable("userId") Long id) {
        UserDto user = userService.findById(id);
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