package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmLikeRepository;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final FilmLikeRepository filmLikeRepository;

    @Override
    public void addLike(long filmId, long userId) {
        filmLikeRepository.addLike(filmId, userId); // Используем репозиторий для добавления лайка
    }

    @Override
    public void removeLike(long filmId, long userId) {
        filmLikeRepository.removeLike(filmId, userId); // Используем репозиторий для удаления лайка
    }
}
