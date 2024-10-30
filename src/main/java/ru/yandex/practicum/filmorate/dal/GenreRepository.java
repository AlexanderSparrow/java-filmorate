package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepository {
    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;

    public List<Genre> findAll() {
        String query = "SELECT * FROM genres";
        return jdbc.query(query, mapper);
    }

    // Найти жанр по ID
    public Optional<Genre> findById(Long id) {
        String query = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbc.query(query, mapper, id);
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.getFirst());
    }

}