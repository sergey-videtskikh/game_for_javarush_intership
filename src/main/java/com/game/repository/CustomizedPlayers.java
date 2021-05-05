package com.game.repository;

import java.util.List;

public interface CustomizedPlayers<T> {
    List<T> getAllPlayersQuery(String name);
}
