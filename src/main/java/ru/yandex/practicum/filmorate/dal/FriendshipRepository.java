package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendshipRepository {
    private final JdbcTemplate jdbcTemplate;

    // Добавить друга
    public void addFriend(long userId, long friendId, int statusId) {
        String sql = "INSERT INTO friendships (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, statusId);
    }

    // Удалить друга
    public void removeFriend(long userId, long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    // Получить список друзей пользователя
    public List<Long> getUserFriends(long userId) {
        String sql = "SELECT friend_id FROM friendships WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    // Получить статус дружбы между пользователями
    public Integer getFriendshipStatus(long userId, long friendId) {
        String sql = "SELECT status_id FROM friendships WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, friendId);
    }
}