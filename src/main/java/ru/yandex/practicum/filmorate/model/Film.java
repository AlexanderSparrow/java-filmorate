package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Setter
@Getter
public class Film {

    private long id;

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
    private Set<Genre> genres = new HashSet<>();

    // Поле для хранения режиссеров фильма
    @NotNull(message = "Режиссер(ы) фильма должен быть указан")
    private Set<Director> directors = new HashSet<>();

    // Поле для хранения рейтинга Ассоциации кинокомпаний
    @NotNull(message = "Рейтинг фильма должен быть указан")
    private Mpa mpa;

    // Поле для хранения ID пользователей, поставивших лайк фильму
    private Set<Long> userIds = new HashSet<>();

    public int getRate() {
        return userIds.size(); // Количество лайков
    }
}
