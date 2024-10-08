package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        log.info("Добавление друга {} к пользователю {}", friendId, userId);
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendId, userId);
        user.getFriends().add(friendId);  // Добавляем друга пользователю
        log.info("Пользователь {} добавлен в друзья пользователю {}", userId, friendId);
        friend.getFriends().add(userId);  // Добавляем пользователя другу
        userStorage.updateUser(user);     // Обновляем пользователя
        userStorage.updateUser(friend);   // Обновляем друга
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));

        user.getFriends().remove(friendId);  // Удаляем друга из списка
        friend.getFriends().remove(userId);  // Удаляем пользователя из списка друга

        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        log.info("Получение спискаобщих друзей для пользователя {} и пользователя {}", userId1, userId2);
        User user1 = userStorage.getUserById(userId1)
                .orElseThrow(() -> new UserNotFoundException(userId1));
        User user2 = userStorage.getUserById(userId2)
                .orElseThrow(() -> new UserNotFoundException(userId2));
        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();
        user1Friends.retainAll(user2Friends); // Оставляем только общих друзей
        return userStorage.getAllUsers().stream()
                .filter(user -> user1Friends.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Optional<User> getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public Set<User> getUserFriends(int userId) {
        log.info("Получение списка друзей для пользователя {}", userId);
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId)
                        .orElseThrow(() -> new UserNotFoundException(friendId))) // Получаем объекты User по ID
                .collect(Collectors.toSet());
    }
}