package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends NotFoundException {

    public FilmNotFoundException(Integer id) {
        super(String.format("Фильм с id=%s не найден", id));
    }
}
