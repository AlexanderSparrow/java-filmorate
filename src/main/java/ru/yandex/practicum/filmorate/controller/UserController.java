package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким id уже существует");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            setDefaultName(user); // Заменяем имя на логин
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            setDefaultName(user); // Заменяем имя на логин
        }
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void setDefaultName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
