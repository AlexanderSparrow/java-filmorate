package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    private final JdbcTemplate jdbcTemplate;

    public FilmRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }    // Инъекция зависимости UserRepository через конструктор

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));

        // Маппинг MPA рейтинга
        Mpa mpa = getMpaRating(resultSet.getInt("mpa_rating_id"));
        film.setMpa(mpa);

        // Маппинг жанров
        Set<Genre> genres = getFilmGenres(film.getId());
        film.setGenres(genres);

        return film;
    }

    private Mpa getMpaRating(int ratingId) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{ratingId}, (rs, rowNum) -> {
            Mpa rating = new Mpa();
            rating.setId(rs.getInt("id"));
            rating.setName(rs.getString("rating"));
            return rating;
        });
    }

    private Set<Genre> getFilmGenres(long filmId) {
        String sql = "SELECT g.id, g.genre FROM genres g JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, new Object[]{filmId}, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("genre"));
            return genre;
        });
        return new HashSet<>(genres);
    }
}
