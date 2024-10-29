package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Setter
@Getter
public class User {

    private long id;

    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "Электронная почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    // Связи с другими пользователями (друзья)
    private Set<Long> friends = new HashSet<>();

    // Связи с другими пользователями (друзья) и их статус дружбы
    //Заготовка для DB
    private Set<Friendship> friendsAndFriendships = new HashSet<>();
}