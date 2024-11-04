package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {

	private Validator validator;

	@BeforeEach
	public void setup() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
	}
	//Film tests
	@Test
	public void shouldFailWhenFilmNameIsEmpty() {
		Film film = new Film();
		film.setName("");
		film.setDescription("Какое-то описание");
		film.setReleaseDate(LocalDate.now());
		film.setDuration(120);

		Set<ConstraintViolation<Film>> violations = validator.validate(film);
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
	public void shouldPassWhenAllFilmFieldsAreValid() {
		Film film = new Film();
		film.setName("Корректное имя");
		film.setDescription("Корректное описание");
		film.setReleaseDate(LocalDate.now());
		film.setDuration(120);

		// Установка MPA рейтинга и множества жанров
		Mpa mpa = new Mpa();
		mpa.setId(1);
		film.setMpa(mpa);
		film.setGenres(Collections.emptySet()); // Пустой набор жанров, но он есть:)

		// Валидация фильма
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertTrue(violations.isEmpty(), "Ожидалось отсутствие ошибок валидации, но были найдены: " + violations);
	}

	//User Tests
	@Test
	public void shouldFailWhenEmailIsInvalid() {
		User user = new User();
		user.setEmail("invalidEmail");
		user.setLogin("validLogin");
		user.setBirthday(LocalDate.now().minusYears(20));

		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	void shouldFailIfEmailIsBlank() {
		User user = new User();
		user.setEmail("");
		user.setLogin("validLogin");
		user.setBirthday(LocalDate.of(2000, 1, 1));

		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void shouldFailWhenLoginContainsSpaces() {
		User user = new User();
		user.setEmail("valid@email.com");
		user.setLogin("invalid login");
		user.setBirthday(LocalDate.now().minusYears(20));

		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

	@Test
	public void shouldPassWhenAllUserFieldsAreValid() {
		User user = new User();
		user.setEmail("valid@email.com");
		user.setLogin("validLogin");
		user.setBirthday(LocalDate.now().minusYears(20));

		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertTrue(violations.isEmpty());
	}

	@Test
	void shouldFailIfBirthdayIsInTheFuture() {
		User user = new User();
		user.setEmail("test@example.com");
		user.setLogin("validLogin");
		user.setBirthday(LocalDate.now().plusDays(1));

		Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
		assertFalse(violations.isEmpty());
	}

}
