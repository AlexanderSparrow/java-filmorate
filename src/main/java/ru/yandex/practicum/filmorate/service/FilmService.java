package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FilmService {
    private static final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final MpaService mpaService;
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingInt(Film::getRate).reversed();

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService, GenreService genreService, MpaService mpaService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

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
        validateUniqueGenresAndMpa(film);  // Проверка на уникальность
        return filmStorage.addFilm(film);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    public Film updateFilm(Film film) {
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new FilmNotFoundException(film.getId()));
        validateUniqueGenresAndMpa(film);  // Проверка на уникальность при обновлении
        return filmStorage.updateFilm(film);
    }

    public void addLike(long filmId, long userId) {
        validateUserAndFilm(filmId, userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getUserIds().add(userId);
        filmStorage.addLike(filmId, userId);//TODO
        filmStorage.updateFilm(film);
    }

    public void removeLike(long filmId, long userId) {
        validateUserAndFilm(filmId, userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getUserIds().remove(userId);
        filmStorage.removeLike(filmId, userId);//TODO
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count).stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilm(long id) {
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
    }

    private void validateUserAndFilm(long filmId, long userId) {
        userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
    }

    private void validateUniqueGenresAndMpa(Film film) {
        Set<Genre> uniqueGenres = new HashSet<>(film.getGenres());
        if (uniqueGenres.size() != film.getGenres().size()) {
            throw new IllegalArgumentException("Жанры фильма должны быть уникальными");
        }
    }
}

