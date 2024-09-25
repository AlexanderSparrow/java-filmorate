package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final Map<Integer, Set<Integer>> userFriends = new HashMap<>();

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        userFriends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        userFriends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId); // Добавляем друга в друзья
    }

    public void removeFriend(int userId, int friendId) {
        Set<Integer> user1Friends = userFriends.get(userId);
        Set<Integer> user2Friends = userFriends.get(friendId);
        if (user1Friends != null) {
            user1Friends.remove(friendId);
        }
        if (user2Friends != null) {
            user2Friends.remove(userId);
        }
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        Set<Integer> user1Friends = getUserFriends(userId1);
        Set<Integer> user2Friends = getUserFriends(userId2);
        user1Friends.retainAll(user2Friends); // Оставляем только общих друзей
        return userStorage.getAllUsers().stream()
                .filter(user -> user1Friends.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Set<Integer> getUserFriends(int userId) {
        return userFriends.getOrDefault(userId, new HashSet<>()); // Возвращаем друзей или пустое множество
    }
}
