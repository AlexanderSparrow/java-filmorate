package ru.yandex.practicum.filmorate.exception;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(final String message) {
        super(message);
    }
}
