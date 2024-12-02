package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.DirectorRowMapper;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorRepository {
    private final JdbcTemplate jdbc;
    private final DirectorRowMapper mapper;

    public Optional<Director> findById(long id) {
        String findByIdQuery = "SELECT * FROM DIRECTORS WHERE ID = ?";
        List<Director> directors = jdbc.query(findByIdQuery, mapper, id);
        if (directors.isEmpty()) {
            throw new DirectorNotFoundException(id);
        }
        return Optional.of(directors.getFirst());
    }

    public List<Director> findAll() {
        String query = "SELECT ID, DIRECTOR FROM DIRECTORS ORDER BY ID";
        return jdbc.query(query, mapper);
    }

    public Director create(Director director) {
        String query = "INSERT INTO DIRECTORS (DIRECTOR) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        Long directorId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new IllegalStateException("Ошибка генерации ID режиссера."));

        director.setId(directorId);
        return director;
    }

    public void delete(long id) {
        String deleteQuery = "DELETE FROM DIRECTORS WHERE id = ?";
        jdbc.update(deleteQuery, id);
        log.info("Удален директор с ID {}", id);
    }

    public Set<Director> getFilmDirectors(long id) {
        String query = """
                SELECT d.ID, d.DIRECTOR FROM DIRECTORS d \
                JOIN FILM_DIRECTORS fd ON d.id = fd.DIRECTOR_ID \
                WHERE fd.FILM_ID = ?""";
        return new HashSet<>(jdbc.query(query, mapper, id));
    }

    public Director update(Director director) {
        String updateQuery = "UPDATE DIRECTORS SET DIRECTOR = ? WHERE id = ?";
        int rowAffected = jdbc.update(updateQuery, director.getName(), director.getId());
        if (rowAffected > 0) {
            return director;
        } else {
            throw new DirectorNotFoundException(director.getId());
        }
    }
}
