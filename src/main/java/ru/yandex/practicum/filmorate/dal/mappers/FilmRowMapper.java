package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final DirectorRepository directorRepository;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));

        // Маппинг MPA рейтинга
        Mpa mpa = mpaRepository.getMpaRating(resultSet.getInt("mpa_rating_id"));
        film.setMpa(mpa);

        // Маппинг жанров
        Set<Genre> genres = genreRepository.getFilmGenres(film.getId());
        film.setGenres(genres);

        //Маппинг режиссеров
        Set < Director> directors = directorRepository.getFilmDirectors(film.getId());
        film.setDirectors(directors);

        return film;
    }
}
