package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {

    private long userId;      // ID пользователя, который отправил запрос на дружбу
    private long friendId;    // ID пользователя, которому был отправлен запрос
}
