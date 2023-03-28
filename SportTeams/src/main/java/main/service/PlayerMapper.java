package main.service;

import main.model.*;
import main.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class PlayerMapper {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    NameMapper nameMapper;

    public Player dtoToPlayer(DtoPlayer dtoPlayer) {
        Player player = new Player();
        player.setId(dtoPlayer.getId());
        player.setSurname(nameMapper.normalName(dtoPlayer.getSurname()));
        player.setName(nameMapper.normalName(dtoPlayer.getName()));
        player.setPatronymic(nameMapper.normalName(dtoPlayer.getPatronymic()));
        player.setBthdate(LocalDate.parse(dtoPlayer.getBthdate()));
        player.setPosition(Position.valueOf(dtoPlayer.getPosition().toUpperCase()));
        Optional<Team> optionalTeam = teamRepository
                .findByName(nameMapper.normalName(dtoPlayer.getTeam()));
        player.setTeamId(optionalTeam.map(Team::getId).orElse(0));
        return player;
    }
}
