package main.service;

import main.model.Team;
import main.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService{

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team findById(Integer id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public Team findByName(String name) {
        return teamRepository.findByName(name).orElse(null);
    }

    @Override
    public boolean existsById(Integer id) {
        return teamRepository.existsById(id);
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team update(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void deleteById(Integer id) {
       teamRepository.deleteById(id);
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }
}
