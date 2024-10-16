package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;


@Component
@Qualifier
public class FilmDbStorage implements FilmStorage{
    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() {
        return List.of();
    }

    @Override
    public void deleteFilm(long id) {

    }
}
