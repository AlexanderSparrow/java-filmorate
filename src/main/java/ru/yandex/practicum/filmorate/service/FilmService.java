package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingInt(Film::getRate).reversed();

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) {
        log.info("Добавление фильма: {}", film);
        validateUniqueGenresAndMpa(film);  // Проверка на уникальность
        return filmStorage.addFilm(film);
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
        filmStorage.updateFilm(film);
    }

    public void removeLike(long filmId, long userId) {
        validateUserAndFilm(filmId, userId);
        Film film = filmStorage.getFilmById(filmId).get();
        film.getUserIds().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
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

