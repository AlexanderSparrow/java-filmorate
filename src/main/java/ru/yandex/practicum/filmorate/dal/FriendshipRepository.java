package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendshipRepository {
    private final JdbcTemplate jdbcTemplate;

    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id, status_id) VALUES (?, ?, 1)";  // Статус 1 = "подтверждённая дружба"
        jdbcTemplate.update(sql, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<Long> getUserFriends(long userId) {
        String sql = "SELECT friend_id FROM friendships WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    public List<Long> getCommonFriends(long userId1, long userId2) {
        String sql = "SELECT f1.friend_id " +
                "FROM friendships f1 " +
                "JOIN friendships f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId1, userId2);
    }
}
