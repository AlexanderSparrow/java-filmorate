-- Инициализация данных для таблицы пользователей
--INSERT INTO users (name, login, email, birthday)
--VALUES ('Иван Иванов', 'ivan_ivanov', 'ivan@example.com', '1990-01-15');

--INSERT INTO users (name, login, email, birthday)
--VALUES ('Анна Смирнова', 'anna_smirnova', 'anna@example.com', '1985-05-30');

--INSERT INTO users (name, login, email, birthday)
--VALUES ('Пётр Петров', 'petr_petrov', 'petr@example.com', '1992-08-20');

-- Инициализация данных для таблицы жанров
--INSERT INTO genres (genre) VALUES ('Комедия');
--INSERT INTO genres (genre) VALUES ('Драма');
--INSERT INTO genres (genre) VALUES ('Мультфильм');
--INSERT INTO genres (genre) VALUES ('Триллер');
--INSERT INTO genres (genre) VALUES ('Документальный');
--INSERT INTO genres (genre) VALUES ('Боевик');

-- Инициализация данных для таблицы рейтингов MPA
--INSERT INTO mpa_ratings (rating) VALUES ('G');
--INSERT INTO mpa_ratings (rating) VALUES ('PG');
--INSERT INTO mpa_ratings (rating) VALUES ('PG-13');
--INSERT INTO mpa_ratings (rating) VALUES ('R');
--INSERT INTO mpa_ratings (rating) VALUES ('NC-17');

-- Инициализация данных для таблицы статусов дружбы
INSERT INTO friendship_status (status) VALUES ('неподтверждённая');
INSERT INTO friendship_status (status) VALUES ('подтверждённая');

-- Инициализация данных для таблицы фильмов
INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
VALUES ('Брат', 'Фильм о непростых 90-х в России', '1997-10-17', 96, 4);

INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
VALUES ('Ирония судьбы, или С лёгким паром!', 'Культовая новогодняя комедия', '1975-12-31', 184, 1);

INSERT INTO films (name, description, release_date, duration, mpa_rating_id)
VALUES ('Операция Ы и другие приключения Шурика', 'Советская комедия о приключениях студента', '1965-07-16', 95, 1);
