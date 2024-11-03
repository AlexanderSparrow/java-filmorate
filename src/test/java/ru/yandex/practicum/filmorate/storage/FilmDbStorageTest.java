package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private FilmDbStorage filmStorage;

    @Autowired
    public void setFilmStorage(final FilmDbStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Test
    @DirtiesContext
    void testAddFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        Genre genre = new Genre();
        genre.setId(1);
        film.setGenres(new HashSet<>());

        Film savedFilm = filmStorage.addFilm(film);
        assertThat(savedFilm).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DirtiesContext
    void testFindFilmById() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmStorage.addFilm(film);

        Optional<Film> foundFilm = filmStorage.getFilmById(1L);
        assertThat(foundFilm)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @DirtiesContext
    void testUpdateFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        Film savedFilm = filmStorage.addFilm(film);

        savedFilm.setName("Updated Film");
        filmStorage.updateFilm(savedFilm);

        Optional<Film> updatedFilm = filmStorage.getFilmById(savedFilm.getId());
        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(f -> assertThat(f).hasFieldOrPropertyWithValue("name", "Updated Film"));
    }

    @Test
    @DirtiesContext
    void testDeleteFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmStorage.addFilm(film);

        filmStorage.deleteFilm(1L);
        Optional<Film> deletedFilm = filmStorage.getFilmById(1L);
        assertThat(deletedFilm).isEmpty();
    }
}
