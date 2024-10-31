package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final FilmLikeRepository filmLikeRepository;
    private final FilmRepository filmRepository;

    @Override
    public Film addFilm(Film film) {
        validateReleaseDate(film);
        filmRepository.save(film);
        return film;
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        filmRepository.update(film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return filmRepository.findById(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public void deleteFilm(long id) {
        filmRepository.deleteById(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        filmLikeRepository.addLike(filmId, userId); // Используем репозиторий для добавления лайка
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmLikeRepository.removeLike(filmId, userId); // Используем репозиторий для удаления лайка
    }
}
