package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(long id);

    List<User> getAllUsers();

    void deleteUser(long id);

    void addFriend(long userId, long friendId, int statusId);

    void removeFriend(long userId, long friendId);

    List<User> getFriends(long userId);
}
