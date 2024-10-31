package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("mpaDbStorage")
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getAllMpa() {
        return mpaRepository.findAll();
    }

    @Override
    public Optional<Mpa> getMpaById(long id) {
        return mpaRepository.findById(id);
    }
}
