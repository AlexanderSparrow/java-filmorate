package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreRepository {
    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;

    public List<Genre> findAll() {
        String query = "SELECT * FROM genres ORDER BY id";
        return jdbc.query(query, mapper);
    }

    public Optional<Genre> findById(Long id) {
        String query = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbc.query(query, mapper, id);
        return genres.isEmpty() ? Optional.empty() : Optional.of(genres.getFirst());
    }

    public Set<Genre> getFilmGenres(long filmId) {
        String sql = "SELECT g.id, g.genre FROM genres g JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbc.query(sql, new Object[]{filmId}, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("genre"));
            return genre;
        });
        return new HashSet<>(genres);
    }
}