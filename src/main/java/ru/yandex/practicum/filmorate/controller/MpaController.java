package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;
    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }
}
