package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmRepository {

    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;

    // Найти все фильмы
    public List<Film> findAll() {
        String query = "SELECT f.*, COUNT(fl.user_id) AS like_count\n" +
                "FROM films f\n" +
                "LEFT JOIN film_likes fl ON f.id = fl.film_id\n" +
                "GROUP BY f.id\n" +
                "ORDER BY like_count DESC\n";
        return jdbc.query(query, mapper);
    }

    // Найти фильм по ID
    public Optional<Film> findById(long id) {
        String query = "SELECT * FROM films WHERE id = ?";
        List<Film> films = jdbc.query(query, mapper, id);
        return films.isEmpty() ? Optional.empty() : Optional.of(films.get(0));
    }

    // Добавить новый фильм
    public Film save(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        updateFilmGenres(film);
        return film;
    }

    // Обновить фильм
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? WHERE id = ?";
        jdbc.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        updateFilmGenres(film);
        return film;
    }

    // Удалить фильм
    public void deleteById(long id) {
        String sql = "DELETE FROM films WHERE id = ?";
        jdbc.update(sql, id);
    }

    // Метод для добавления лайка фильму
    public void addLike(long filmId, long userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbc.update(sql, filmId, userId);
    }

    // Метод для удаления лайка у фильма
    public void removeLike(long filmId, long userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(sql, filmId, userId);
    }

    // Метод для подсчета лайков для фильма
    public int getLikeCount(long filmId) {
        String sql = "SELECT COUNT(*) FROM film_likes WHERE film_id = ?";
        return jdbc.queryForObject(sql, Integer.class, filmId);
    }

    private void updateFilmGenres(Film film) {
        String deleteSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbc.update(deleteSql, film.getId());

        String insertSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbc.update(insertSql, film.getId(), genre.getId());
        }
    }
}
