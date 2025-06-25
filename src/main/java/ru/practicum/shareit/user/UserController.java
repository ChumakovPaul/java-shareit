package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("==> Creating user: {}", userCreateDto);
        UserDto user = userService.createUser(userCreateDto);
        log.info("<== Creating user: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long userId) {
        log.info("==> Updating user: {}", userUpdateDto);
        UserDto user = userService.updateUser(userUpdateDto, userId);
        log.info("<== Updating user: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("==> Getting user:{}", userId);
        UserDto user = userService.getUser(userId);
        log.info("<== Getting user:{}", userId);
        return user;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("==> Getting all users");
        List<UserDto> users = userService.getAllUsers();
        log.info("<== Getting all users");
        return users;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("==> Delete user:{}", userId);
        userService.deleteUser(userId);
        log.info("<== Delete user:{}", userId);
    }
}