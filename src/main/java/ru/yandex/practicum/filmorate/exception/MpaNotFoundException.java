package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends NotFoundException {

    public MpaNotFoundException(Long id) {
        super(String.format("Рейтинг с id=%s не найден", id));
    }
}
