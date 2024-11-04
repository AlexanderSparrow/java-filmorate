package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRepository mpaRepository;

    public List<Mpa> getAllMpa() {
        return mpaRepository.findAll();
    }

    public Mpa getMpaById(long id) {
        return mpaRepository.findById(id)
                .orElseThrow(() -> new MpaNotFoundException(id));
    }

    public boolean existsById(long id) {
        return mpaRepository.findById(id).isPresent();
    }
}
