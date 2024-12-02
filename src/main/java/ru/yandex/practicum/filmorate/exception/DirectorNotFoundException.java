package ru.yandex.practicum.filmorate.exception;

public class DirectorNotFoundException extends NotFoundException {

    public DirectorNotFoundException(Long id) {
        super(String.format("Директор с id=%s не найден", id));
    }
}
