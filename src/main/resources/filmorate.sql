CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,
    mpa_rating_id INT REFERENCES mpa_ratings(id)  -- Добавляем внешний ключ для рейтингов MPA
);

CREATE TABLE IF NOT EXISTS genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    genre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rating VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS friendship_status (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,   -- При удалении пользователя удаляем и его друзей
    friend_id INT REFERENCES users(id) ON DELETE CASCADE, -- При удалении друга, удаляем связи
    status_id INT REFERENCES friendship_status(id),
    PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT REFERENCES films(id) ON DELETE CASCADE,  -- Если фильм удалён, удаляются и его жанры
    genre_id INT REFERENCES genres(id) ON DELETE CASCADE, -- Если жанр удалён, удаляются все записи
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INT REFERENCES films(id) ON DELETE CASCADE,  -- Если фильм удалён, удаляются и лайки
    user_id INT REFERENCES users(id) ON DELETE CASCADE,  -- Если пользователь удалён, удаляются и его лайки
    PRIMARY KEY (film_id, user_id)
);
