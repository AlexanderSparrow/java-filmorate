package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final FilmRepository filmRepository;
    private final LikeDbStorage likeDbStorage;

    @Override
    public Film addFilm(Film film) {
        filmRepository.save(film);
        return film;
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
        likeDbStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        likeDbStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmRepository.getPopularFilms(count);
    }
}
