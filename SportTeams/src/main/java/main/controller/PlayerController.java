package main.controller;

import main.model.*;
import main.model.dto.DtoPlayer;
import main.model.mapper.NameMapper;
import main.model.mapper.PlayerMapper;
import main.service.PlayerService;
import main.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    NameMapper nameMapper;

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(
                       @RequestParam(defaultValue = "") String positionName
                             , @RequestParam(defaultValue = "") String teamName){
        List<Player> list = new ArrayList<>(playerService.findAll());
        if (!positionName.isEmpty()) {
            Position position;
            try {
                position = Position.valueOf(positionName.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            list = list.stream().filter(p -> p.getPosition() == position).collect(Collectors.toList());
        }
        if (!teamName.isEmpty()) {
            Team team = teamService.findByName(nameMapper.normalName(teamName));
            if (team == null) {
                return ResponseEntity.badRequest().build();
            }
            list = list.stream().filter(p -> p.getTeamId() == team.getId()).collect(Collectors.toList());
        }
        list.sort(Comparator.comparing(Player::getId));
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable int id) {
        Player player = playerService.findById(id);
        return (player == null) ? ResponseEntity.notFound().build() :
                new ResponseEntity(player, HttpStatus.OK);
    }

    @PostMapping("/players")
    public Player addPlayer(DtoPlayer dtoPlayer) {
        return playerService.save(playerMapper.dtoToPlayer(dtoPlayer));
    }

    @PatchMapping("/players/{id}")
    public ResponseEntity<Player> correctPlayer(@PathVariable int id, String surname,
            String name, String patronymic, String bthdate, String position, String teamName) {
        Player player = playerService.findById(id);
        if(player == null) {
            return ResponseEntity.notFound().build();
        }
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
        if (!teamName.isEmpty()) {
            Team team = teamService.findByName(nameMapper.normalName(teamName));
            player.setTeamId((team != null) ? team.getId() : 0);
        }
        return new ResponseEntity(playerService.save(player), HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public HttpStatus deletePlayer(@PathVariable int id) {
        if (playerService.existsById(id)) {
            playerService.deleteById(id);
            return HttpStatus.OK;
        }
        return HttpStatus.NO_CONTENT;
    }
}
