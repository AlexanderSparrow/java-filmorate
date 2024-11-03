package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendshipRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;


    // Добавить друга
    public void addFriend(long userId, long friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    // Удалить друга
    public void removeFriend(long userId, long friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    public List<User> getUserFriends(long userId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN friendships f ON u.id = f.friend_id " +
                "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, userRowMapper);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        String sql = "SELECT * FROM USERS u " +
                "JOIN FRIENDSHIPS f1 ON u.id = f1.friend_id " +
                "JOIN FRIENDSHIPS f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId, friendId}, userRowMapper);
    }
}