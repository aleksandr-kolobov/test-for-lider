package main.controller;

import main.model.DtoTeam;
import main.model.Sport;
import main.model.Team;
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

@RestController
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/teams")
    public List<Team> getTeamsList(@RequestParam(defaultValue = "ALL") String sport
            , @RequestParam(defaultValue = "0000-01-01") String fromdate
            , @RequestParam(defaultValue = "9999-12-31") String tilldate){
        sport = sport.toUpperCase();
        Sport sportF = Sport.valueOf(sport);
        LocalDate fromDate = LocalDate.parse(fromdate);
        LocalDate tillDate = LocalDate.parse(tilldate);
        List<Team> list = new ArrayList<>();
        teamRepository.findAll().stream().filter(t -> (t.getBthdate().isAfter(fromDate))
                        && (t.getBthdate().isBefore(tillDate))
                        && (sportF.equals(Sport.ALL) || sportF.equals(t.getSport())))
                .forEach(list::add);
        list.sort(Comparator.comparing(Team::getId));
        return list;
    }

    @GetMapping("/teams/{nameOrId}")
    public ResponseEntity<Team> getTeam(@PathVariable String nameOrId) {
        nameOrId = nameOrId.substring(0, 1).toUpperCase() + nameOrId.substring(1).toLowerCase();
        Optional<Team> optional = teamRepository.findByName(nameOrId);
        if (optional.isPresent()) {
            return new ResponseEntity(optional.get(), HttpStatus.OK);
        }
        optional = teamRepository.findById(Integer.parseInt(nameOrId));
        return !optional.isPresent() ? ResponseEntity.notFound().build() :
                new ResponseEntity(optional.get(), HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity addTeam(DtoTeam dtoTeam) {
        Team team = new Team();
        String name = dtoTeam.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        team.setName(name);
        team.setSport(Sport.valueOf(dtoTeam.getSport().toUpperCase()));
        team.setBthdate(LocalDate.parse(dtoTeam.getBthdate()));
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @PatchMapping("/teams")
    public ResponseEntity<Team> correctTeamByName(String name, String sport, String bthdate) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        Optional<Team> optional = teamRepository.findByName(name);
        if(optional.isPresent()) {
            Team team = optional.get();
            team.setSport(Sport.valueOf(sport.toUpperCase()));
            team.setBthdate(LocalDate.parse(bthdate));
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/teams/{id}")
    public ResponseEntity<Team> correctTeamById(@PathVariable int id,
                             String name, String sport, String bthdate) {
        Optional<Team> optional = teamRepository.findById(id);
        if(optional.isPresent()) {
            Team team = optional.get();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            team.setName(name);
            team.setSport(Sport.valueOf(sport.toUpperCase()));
            team.setBthdate(LocalDate.parse(bthdate));
            return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity deleteTeam(@PathVariable int id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
