package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание фильма не может быть длиннее 200 символов")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;

    // Поле для хранения жанров фильма
    @NotNull(message = "Жанр фильма должен быть указан")
    private Set<Genre> genres;

    // Поле для хранения рейтинга Ассоциации кинокомпаний
    @NotNull(message = "Рейтинг фильма должен быть указан")
    private MpaRating mpaRating;

    // Поле для хранения ID пользователей, поставивших лайк фильму
    private Set<Integer> userIds = new HashSet<>();

    public int getRate() {
        return userIds.size(); // Количество лайков
    }
}
