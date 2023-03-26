package main.controller;

import main.model.*;
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
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeamsList(@RequestParam(defaultValue = "") String sport
                         , @RequestParam(defaultValue = "") String fromdate
                   , @RequestParam(defaultValue = "") String tilldate){
        List<Team> list = new ArrayList<>(teamRepository.findAll());
        if (!sport.isEmpty()) {
            Sport sportF;
            try {
                sportF = Sport.valueOf(sport.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            Stream<Team> stream = list.stream().filter(t -> t.getSport() == sportF);
            list = stream.collect(Collectors.toList());
        }
        if (!fromdate.isEmpty()) {
            System.out.println(fromdate);
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(fromdate);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            Stream<Team> stream = list.stream().filter(t -> t.getBthdate().isAfter(localDate));
            list = stream.collect(Collectors.toList());
        }
        if (!tilldate.isEmpty()) {
            System.out.println(tilldate);
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(tilldate);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            Stream<Team> stream = list.stream().filter(t -> t.getBthdate().isBefore(localDate));
            list = stream.collect(Collectors.toList());
        }
        list.sort(Comparator.comparing(Team::getId));
        return new ResponseEntity(list, HttpStatus.OK);
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
    public ResponseEntity<Team> addTeam(DtoTeam dtoTeam) {
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
        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Team team = optional.get();
        if (!sport.isEmpty()) {
            team.setSport(Sport.valueOf(sport.toUpperCase()));
        }
        if (!bthdate.isEmpty()) {
            team.setBthdate(LocalDate.parse(bthdate));
        }
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
    }

    @PatchMapping("/teams/{id}")
    public ResponseEntity<Team> correctTeamById(@PathVariable int id,
                             String name, String sport, String bthdate) {
        Optional<Team> optional = teamRepository.findById(id);
        if(!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Team team = optional.get();
        if (!name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            team.setName(name);
        }
        if (!sport.isEmpty()) {
            team.setSport(Sport.valueOf(sport.toUpperCase()));
        }
        if (!bthdate.isEmpty()) {
            team.setBthdate(LocalDate.parse(bthdate));
        }
        return new ResponseEntity(teamRepository.save(team), HttpStatus.OK);
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
