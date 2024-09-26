package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new FilmNotFoundException(filmId);
        }
        if (userService.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        film.getUserIds().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(int filmId, int userId) {
        if (userService.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        if (userService.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(filmId));
        film.getUserIds().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> {
                    int likesComparison = Integer.compare(film2.getUserIds().size(), film1.getUserIds().size());
                    return likesComparison != 0 ? likesComparison : Integer.compare(film1.getId(), film2.getId());
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }
}
