package main.controller;

import main.model.*;
import main.model.dto.DtoTeam;
import main.model.mapper.NameMapper;
import main.model.mapper.TeamMapper;
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
import java.util.stream.Stream;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    NameMapper nameMapper;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeamsList(@RequestParam(defaultValue = "") String sport
                         , @RequestParam(defaultValue = "") String fromdate
                   , @RequestParam(defaultValue = "") String tilldate){
        List<Team> list = new ArrayList<>(teamService.findAll());
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
        nameOrId = nameMapper.normalName(nameOrId);
        Team team = teamService.findByName(nameOrId);
        if (team != null) {
            return new ResponseEntity(team, HttpStatus.OK);
        }
        team = teamService.findById(Integer.parseInt(nameOrId));
        return (team == null) ? ResponseEntity.notFound().build() :
                new ResponseEntity(team, HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity<Team> addTeam(DtoTeam dtoTeam) {
        Team team = teamMapper.dtoToTeam(dtoTeam);
        return new ResponseEntity(teamService.save(team), HttpStatus.OK);
    }

    @PatchMapping("/teams")
    public ResponseEntity<Team> correctTeamByName(String name, String sport, String bthdate) {
        Team team = teamService.findByName(nameMapper.normalName(name));
        if(team == null) {
            return ResponseEntity.notFound().build();
        }
        if (!sport.isEmpty()) {
            team.setSport(Sport.valueOf(sport.toUpperCase()));
        }
        if (!bthdate.isEmpty()) {
            team.setBthdate(LocalDate.parse(bthdate));
        }
        return new ResponseEntity(teamService.save(team), HttpStatus.OK);
    }

    @PatchMapping("/teams/{id}")
    public ResponseEntity<Team> correctTeamById(@PathVariable int id,
                             String name, String sport, String bthdate) {
        Team team = teamService.findById(id);
        if(team == null) {
            return ResponseEntity.notFound().build();
        }
        if (!name.isEmpty()) {
            team.setName(nameMapper.normalName(name));
        }
        if (!sport.isEmpty()) {
            team.setSport(Sport.valueOf(sport.toUpperCase()));
        }
        if (!bthdate.isEmpty()) {
            team.setBthdate(LocalDate.parse(bthdate));
        }
        return new ResponseEntity(teamService.save(team), HttpStatus.OK);
    }

    @DeleteMapping("/teams/{id}")
    public HttpStatus deleteTeam(@PathVariable int id) {
        if (teamService.existsById(id)) {
            teamService.deleteById(id);
            return HttpStatus.OK;
        }
        return HttpStatus.NO_CONTENT;
    }
}
