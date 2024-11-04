package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
    private final UserService userService;
    private final GenreService genreService;
    private final MpaService mpaService;
    private final FilmRepository filmRepository;
    private final FilmLikeRepository filmLikeRepository;

    public Film addFilm(Film film) {
        log.info("Добавление фильма: {}", film);
        validateReleaseDate(film);
        if (!mpaService.existsById(film.getMpa().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Указанный MPA рейтинг не существует");
        }
        for (Genre genre : film.getGenres()) {
            if (!genreService.existsById(genre.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Жанр с ID " + genre.getId() + " не существует");
            }
        }
        return filmRepository.save(film);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    public Film updateFilm(Film film) {
        filmRepository.findById(film.getId())
                .orElseThrow(() -> new FilmNotFoundException(film.getId()));
        return filmRepository.update(film);
    }

    public void addLike(long filmId, long userId) {
        validateUserAndFilm(filmId, userId);
        Film film = filmRepository.findById(filmId).get();
        film.getUserIds().add(userId);
        filmLikeRepository.addLike(filmId, userId);
        filmRepository.update(film);
    }

    public void removeLike(long filmId, long userId) {
        validateUserAndFilm(filmId, userId);
        Film film = filmRepository.findById(filmId).get();
        film.getUserIds().remove(userId);
        filmLikeRepository.removeLike(filmId, userId);
        filmRepository.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmRepository.getPopularFilms(count);
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public void deleteFilm(long id) {
        filmRepository.deleteById(id);
    }

    public Film getFilmById(long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
    }

    private void validateUserAndFilm(long filmId, long userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }
}

