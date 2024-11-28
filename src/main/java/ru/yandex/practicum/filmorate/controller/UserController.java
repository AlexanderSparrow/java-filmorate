package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя id: {}, логином: {}.",
                user.getId(),
                user.getLogin());
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя id: {}, логин: {}.",
                user.getId(),
                user.getLogin());
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") long id) {
        log.info("Получен запрос на получение пользователя с id: {}.", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение списка всех пользователей.");
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на добавление пользователю с id: {}, друга с id: {}.", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на удаление у пользователя с id: {}, друга с id: {}.", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriends(@PathVariable Long userId) {
        log.info("Получен запрос на получение списка друзей пользователя с id: {}.", userId);
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId1}/friends/common/{userId2}")
    public List<User> getCommonFriends(@PathVariable Long userId1, @PathVariable Long userId2) {
        log.info("Получен запрос на получение общих друзей пользователя {} и {}", userId1, userId2);
        return userService.getCommonFriends(userId1, userId2);
    }
}
