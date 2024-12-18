package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
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
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    public void delete(long id) {

        String sql = "DELETE FROM users WHERE id = ?";
        jdbc.update(sql, id);
        log.info("Удален пользователь: id={}", id);
    }

    public User update(User user) {
        String sql = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbc.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        log.info("Обновлен пользователь: id={}", user.getId());
        return user;
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        log.info("Список пользователей:");
        return jdbc.query(query, mapper);
    }

    public Optional<User> findById(long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        log.info("Найден пользователь с id={}", id);
        return jdbc.query(query, mapper, id).stream().findFirst();
    }
}
