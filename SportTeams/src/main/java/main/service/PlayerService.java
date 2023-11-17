package main.service;

import main.model.Player;

import java.util.List;

public interface PlayerService {
    Player findById(Integer id);

    boolean existsById(Integer id);

    Player save(Player player);

    Player update(Player player);

    void deleteById(Integer id);

    List<Player> findAll();
}
