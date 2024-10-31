package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    public User add(User user) {
        String sql = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    public void delete(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbc.update(sql, id);
    }

    public User update(User user) {
        String sql = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbc.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, mapper);
    }

    public Optional<User> findById(long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbc.query(query, mapper, id).stream().findFirst();
    }

    // Получаем список идентификаторов друзей пользователя
    public Set<Long> getUserFriends(long userId) {
        String query = "SELECT friend_id FROM friendships WHERE user_id = ?";
        return new HashSet<>(jdbc.queryForList(query, Long.class, userId));
    }

    public void addFriend(long userId, long friendId, int statusId) {
        String query = "INSERT INTO friendships (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        jdbc.update(query, userId, friendId, statusId);
    }

    public void removeFriend(long userId, long friendId) {
        String query = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        jdbc.update(query, userId, friendId);
    }

    public User getUserById(long id) {
        User user = jdbc.queryForObject("SELECT * FROM users WHERE id = ?", new Object[]{id}, mapper);
        Set<Long> friends = this.getUserFriends(id);  // Получаем список друзей в сервисе
        assert user != null;
        user.setFriends(friends);
        return user;
    }
}
