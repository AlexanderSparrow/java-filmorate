package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationDbTests {
    private UserRepository userRepository;
    private FilmRepository filmRepository;

    @Autowired
    public void setUserUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setFilmRepository(final FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Test
    @DirtiesContext
        // Сброс контекста после теста
    void testAddUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userRepository.add(user);
        assertThat(savedUser).hasFieldOrPropertyWithValue("email", "test@example.com");
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DirtiesContext
        // Сброс контекста после теста
    void testFindUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userRepository.add(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u).hasFieldOrPropertyWithValue("email", "test@example.com"));
    }

    @Test
    @DirtiesContext
        // Сброс контекста после теста
    void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userRepository.add(user);

        savedUser.setName("Updated Name");
        userRepository.update(savedUser);

        Optional<User> updatedUser = userRepository.findById(savedUser.getId());
        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u).hasFieldOrPropertyWithValue("name", "Updated Name"));
    }

    @Test
    @DirtiesContext
        // Сброс контекста после теста
    void testDeleteUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userRepository.add(user);

        userRepository.delete(savedUser.getId());
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
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

        Film savedFilm = filmRepository.save(film);
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
        filmRepository.save(film);

        Optional<Film> foundFilm = filmRepository.findById(1L);
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
        Film savedFilm = filmRepository.save(film);

        savedFilm.setName("Updated Film");
        filmRepository.update(savedFilm);

        Optional<Film> updatedFilm = filmRepository.findById(savedFilm.getId());
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
        filmRepository.save(film);

        filmRepository.deleteById(1L);
        Optional<Film> deletedFilm = filmRepository.findById(1L);
        assertThat(deletedFilm).isEmpty();
    }
}

