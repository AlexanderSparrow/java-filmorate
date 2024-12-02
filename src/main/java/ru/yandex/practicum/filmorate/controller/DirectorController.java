package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")

public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public ResponseEntity<Director> createDirector(@Valid @RequestBody Director director) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.createDirector(director));
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable long id) {
        directorService.delete(id);
    }


    @GetMapping
    public ResponseEntity<List<Director>> getAllDirectors() {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.getAllDirectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Director>> getDirectorById(@PathVariable long id) {
        Optional<Optional<Director>> directorOptional = Optional.ofNullable(directorService.getDirectorById(id));
        return directorOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    public ResponseEntity<Director> updateDirector(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.updateDirector(director);
        return ResponseEntity.ok(updatedDirector);
    }
}
