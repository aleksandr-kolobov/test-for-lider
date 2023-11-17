package main.service;

import main.model.Team;

import java.util.List;

public interface TeamService {
    Team findById(Integer id);

    Team findByName(String name);

    boolean existsById(Integer id);

    Team save(Team team);

    Team update(Team team);

    void deleteById(Integer id);

    List<Team> findAll();
}
