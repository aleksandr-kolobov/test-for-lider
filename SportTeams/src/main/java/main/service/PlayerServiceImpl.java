package main.service;

import main.model.Player;
import main.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player findById(Integer id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Integer id) {
        return playerRepository.existsById(id);
    }

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player update(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deleteById(Integer id) {
        playerRepository.deleteById(id);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll();
    }
}
