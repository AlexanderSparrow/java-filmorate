package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Genre {

    private int id;  // Идентификатор жанра

    @NotBlank(message = "Название жанра не может быть пустым")
    private String genre;  // Название жанра
}
