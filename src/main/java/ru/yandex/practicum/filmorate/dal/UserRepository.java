package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;
//    private final UserRepository userRepository;

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

/*    public User getUserById(long id) {
        User user = jdbc.queryForObject("SELECT * FROM users WHERE id = ?", new Object[]{id}, mapper);
        Set<Long> friends = userRepository.getUserFriends(id);  // Получаем список друзей в сервисе
        user.setFriends(friends);
        return user;
    }*/
}
