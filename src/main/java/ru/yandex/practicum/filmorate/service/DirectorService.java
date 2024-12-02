package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public Director createDirector(Director director) {
        return directorRepository.create(director);
    }

    public Optional<Director> getDirectorById(long id) {
        return directorRepository.findById(id);
    }

    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    public Director updateDirector(Director director) {
        return directorRepository.update(director);
    }

    public void delete(long id) {
        directorRepository.delete(id);
    }
}
