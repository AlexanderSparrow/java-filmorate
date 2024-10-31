package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
