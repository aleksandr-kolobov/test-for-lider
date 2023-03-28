package main.controller;

import main.model.*;
import main.service.NameMapper;
import main.service.PlayerMapper;
import main.repository.PlayerRepository;
import main.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    NameMapper nameMapper;


    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(
                       @RequestParam(defaultValue = "") String position
                             , @RequestParam(defaultValue = "") String team){
        List<Player> list = new ArrayList<>(playerRepository.findAll());
        if (!position.isEmpty()) {
            Position positionF;
            try {
                positionF = Position.valueOf(position.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            Stream<Player> stream = list.stream().filter(p -> p.getPosition() == positionF);
            list = stream.collect(Collectors.toList());
        }
        if (!team.isEmpty()) {
            Optional<Team> optionalTeam = teamRepository.findByName(nameMapper.normalName(team));
            if (!optionalTeam.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            int teamId = optionalTeam.get().getId();
            Stream<Player> stream = list.stream().filter(p -> p.getTeamId() == teamId);
            list = stream.collect(Collectors.toList());
        }
        list.sort(Comparator.comparing(Player::getId));
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable int id) {
        Optional<Player> optional = playerRepository.findById(id);
        return !optional.isPresent() ? ResponseEntity.notFound().build() :
                new ResponseEntity(optional.get(), HttpStatus.OK);
    }

    @PostMapping("/players")
    public Player addPlayer(DtoPlayer dtoPlayer) {
        return playerRepository.save(playerMapper.dtoToPlayer(dtoPlayer));
    }

    @PatchMapping("/players/{id}")
    public ResponseEntity<Player> correctPlayer(@PathVariable int id, String surname,
            String name, String patronymic, String bthdate, String position, String team) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if(!optionalPlayer.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Player player = optionalPlayer.get();
        if (!surname.isEmpty()) {
            player.setSurname(nameMapper.normalName(surname));
        }
        if (!name.isEmpty()) {
            player.setName(nameMapper.normalName(name));
        }
        if (!patronymic.isEmpty()) {
            player.setPatronymic(nameMapper.normalName(patronymic));
        }
        if (!bthdate.isEmpty()) {
            player.setBthdate(LocalDate.parse(bthdate));
        }
        if (!position.isEmpty()) {
            player.setPosition(Position.valueOf(position.toUpperCase()));
        }
        if (!team.isEmpty()) {
            Optional<Team> optionalTeam = teamRepository.findByName(nameMapper.normalName(team));
            player.setTeamId(optionalTeam.isPresent() ? optionalTeam.get().getId() : 0);
        }
        return new ResponseEntity(playerRepository.save(player), HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable int id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
