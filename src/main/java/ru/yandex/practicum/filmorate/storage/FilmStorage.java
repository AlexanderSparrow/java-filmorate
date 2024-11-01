package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(long id);

    List<Film> getAllFilms();

    void deleteFilm(long id);

    void addLike(long filmId, long likeId);

    void removeLike(long filmId, long likeId);

    List<Film> getPopularFilms(int count);
}
