package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void shouldFailWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        Set violations = validator.validate(user);
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

        Set violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldPassWhenAllFieldsAreValid() {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        Set violations = validator.validate(user);
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
