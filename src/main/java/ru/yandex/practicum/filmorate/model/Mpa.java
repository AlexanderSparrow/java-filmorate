package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class Mpa {

    private int id;  // Идентификатор рейтинга

    @NotNull(message = "Рейтинг должен иметь название")
    private String rating;  // Название рейтинга, например, G, PG, PG-13 и т.д.
}
