package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.util.List;

    @Repository
    @RequiredArgsConstructor
    public class MpaRepository {
        private final JdbcTemplate jdbc;
        private final MpaRowMapper mapper;

        public List<Mpa> findAll() {
            String query = "SELECT * FROM mpa_rating";
            return jdbc.query(query, mapper);
        }

    }
