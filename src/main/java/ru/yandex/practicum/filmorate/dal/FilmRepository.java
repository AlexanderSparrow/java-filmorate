package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


    @Repository
    @RequiredArgsConstructor
    public class FilmRepository {
        private final JdbcTemplate jdbc;
        private final FilmRowMapper mapper;

        public List<Film> findAll() {
            String query = "SELECT * FROM films";
            return jdbc.query(query, mapper);
        }
}
