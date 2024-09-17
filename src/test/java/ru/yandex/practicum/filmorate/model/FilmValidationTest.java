package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void shouldFailWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Какое-то описание");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldFailWhenDescriptionIsTooLong() {
        Film film = new Film();
        film.setName("Корректное имя");
        film.setDescription("A".repeat(201)); // 201 chars
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldPassWhenAllFieldsAreValid() {
        Film film = new Film();
        film.setName("Корректное имя");
        film.setDescription("Корректное описание");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        Set<jakarta.validation.ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}
