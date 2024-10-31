package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaRepository {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;

    public List<Mpa> findAll() {
        String query = "SELECT * FROM mpa_ratings ORDER BY id";
        return jdbc.query(query, mapper);
    }

    // Найти рейтинг по ID
    public Optional<Mpa> findById(Long id) {
        String query = "SELECT * FROM mpa_ratings WHERE id = ?";
        List<Mpa> mpaList = jdbc.query(query, mapper, id);
        return mpaList.isEmpty() ? Optional.empty() : Optional.of(mpaList.getFirst());
    }
}
