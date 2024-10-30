package ru.yandex.practicum.filmorate.exception;

public class GenreNotFoundException extends NotFoundException {

    public GenreNotFoundException(Long id) {
        super(String.format("Жанр с id=%s не найден", id));
    }
}
