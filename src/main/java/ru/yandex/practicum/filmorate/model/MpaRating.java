package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MpaRating {

    private int id;  // Идентификатор рейтинга

    @NotBlank(message = "Рейтинг должен иметь название")
    private String rating;  // Название рейтинга, например, G, PG, PG-13 и т.д.
}
