package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import java.util.Set;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    private final UserRepository userRepository;

    // Инъекция зависимости UserRepository через конструктор
    public UserRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());

        // Получаем список друзей для пользователя
        Set<Long> friends = userRepository.getUserFriends(user.getId());
        user.setFriends(friends);

        return user;
    }
}
