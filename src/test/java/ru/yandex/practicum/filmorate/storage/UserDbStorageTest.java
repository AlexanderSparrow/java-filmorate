package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private UserDbStorage userStorage;

    @Autowired
    public void setUserStorage(final UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Test
    @DirtiesContext // Сброс контекста после теста
    void testAddUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userStorage.addUser(user);
        assertThat(savedUser).hasFieldOrPropertyWithValue("email", "test@example.com");
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    @DirtiesContext // Сброс контекста после теста
    void testFindUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userStorage.addUser(user);

        Optional<User> foundUser = userStorage.getUserById(savedUser.getId());
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u).hasFieldOrPropertyWithValue("email", "test@example.com"));
    }

    @Test
    @DirtiesContext // Сброс контекста после теста
    void testUpdateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userStorage.addUser(user);

        savedUser.setName("Updated Name");
        userStorage.updateUser(savedUser);

        Optional<User> updatedUser = userStorage.getUserById(savedUser.getId());
        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u).hasFieldOrPropertyWithValue("name", "Updated Name"));
    }

    @Test
    @DirtiesContext // Сброс контекста после теста
    void testDeleteUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testUser");
        user.setName("Test");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        User savedUser = userStorage.addUser(user);

        userStorage.deleteUser(savedUser.getId());
        Optional<User> deletedUser = userStorage.getUserById(savedUser.getId());
        assertThat(deletedUser).isEmpty();
    }
}
