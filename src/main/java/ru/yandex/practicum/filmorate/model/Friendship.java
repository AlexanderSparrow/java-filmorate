package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {

    private int userId;      // ID пользователя, который отправил запрос на дружбу
    private int friendId;    // ID пользователя, которому был отправлен запрос
    private FriendshipStatus status;  // Статус дружбы (подтверждённая или неподтверждённая)
}
