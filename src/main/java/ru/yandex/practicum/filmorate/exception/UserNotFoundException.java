package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(long id) {
        super(String.format("Пользователь с id=%s не найден", id));
    }
}