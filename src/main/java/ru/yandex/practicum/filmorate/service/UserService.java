package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.DuplicateKeyException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public User addUser(User user) {
        try {
            return userRepository.add(user);
        } catch (DuplicateKeyException e) {
            throw new ValidationException("Email already exists: " + user.getEmail());
        }
    }

    public Optional<User> updateUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
        return Optional.ofNullable(userRepository.update(user));
    }

    public void addFriend(long userId, long friendId) {
        log.info("Добавление друга {} к пользователю {}", friendId, userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));

        if (friendshipRepository.isFriend(userId, friendId)) {
            throw new DuplicateKeyException("Друг уже добавлен");
        }

        friendshipRepository.addFriend(userId, friendId);
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendId, userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException(friendId));
        friendshipRepository.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        return friendshipRepository.getCommonFriends(userId1, userId2);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public List<User> getUserFriends(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return friendshipRepository.getUserFriends(userId);
    }
}

