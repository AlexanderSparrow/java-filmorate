package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.exception.DuplicateKeyException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
//@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final FriendshipRepository friendshipRepository;


    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendshipRepository friendshipRepository) {
        this.userStorage = userStorage;
        this.friendshipRepository = friendshipRepository;
    }

    public User addUser(User user) {
        try {
            return userStorage.addUser(user);
        } catch (DuplicateKeyException e) {
            throw new ValidationException("Email already exists: " + user.getEmail());
        }
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
        return userStorage.updateUser(user);
    }

    public void addFriend(long userId, long friendId) {
        log.info("Добавление друга {} к пользователю {}", friendId, userId);
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));
        if (user.getFriends().contains(friendId)) {
            throw new DuplicateKeyException("Друг уже добавлен");
        }
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendId, userId);
        user.getFriends().add(friendId);  // Добавляем друга пользователю
        log.info("Пользователь {} добавлен в друзья пользователю {}", userId, friendId);
        friend.getFriends().add(userId);  // Добавляем пользователя другу
        friendshipRepository.addFriend(userId, friendId, 1);; //TODO
        userStorage.updateUser(user);     // Обновляем пользователя
        userStorage.updateUser(friend);   // Обновляем друга
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));

        user.getFriends().remove(friendId);  // Удаляем друга из списка
        friend.getFriends().remove(userId);  // Удаляем пользователя из списка друга
        friendshipRepository.removeFriend(userId, friendId);//TODO
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        log.info("Получение списка общих друзей для пользователя {} и пользователя {}", userId1, userId2);
        User user1 = userStorage.getUserById(userId1)
                .orElseThrow(() -> new UserNotFoundException(userId1));
        User user2 = userStorage.getUserById(userId2)
                .orElseThrow(() -> new UserNotFoundException(userId2));
        Set<Long> user1Friends = user1.getFriends();
        Set<Long> user2Friends = user2.getFriends();
        user1Friends.retainAll(user2Friends); // Оставляем только общих друзей
        return userStorage.getAllUsers().stream()
                .filter(user -> user1Friends.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public Set<User> getUserFriends(Long userId) {
        log.info("Получение списка друзей для пользователя {}", userId);
        return friendshipRepository.getUserFriends(userId).stream()
                .map(friendId -> userStorage.getUserById(friendId)
                        .orElseThrow(() -> new UserNotFoundException(friendId)))
                .collect(Collectors.toSet());
    }

    public Set<User> getUserFriends_Old(Long userId) {
        log.info("Получение списка друзей для пользователя {}", userId);
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId)
                        .orElseThrow(() -> new UserNotFoundException(friendId))) // Получаем объекты User по ID
                .collect(Collectors.toSet());

    }

}

