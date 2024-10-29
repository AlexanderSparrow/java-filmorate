package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {

    // Инъекция зависимости UserRepository через конструктор
    public MpaRowMapper(@Lazy MpaRepository mpaRepository) {
    }

    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("id"));
        mpa.setRating(resultSet.getString("rating"));

        return mpa;
    }
}
