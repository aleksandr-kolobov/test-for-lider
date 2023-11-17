package main.model.mapper;

import main.model.dto.DtoTeam;
import main.model.Sport;
import main.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class TeamMapper {
    @Autowired
    NameMapper nameMapper;

    public Team dtoToTeam(DtoTeam dtoTeam) {
        Team team = new Team();
        team.setId(dtoTeam.getId());
        team.setName(nameMapper.normalName(dtoTeam.getName()));
        team.setSport(Sport.valueOf(dtoTeam.getSport().toUpperCase()));
        team.setBthdate(LocalDate.parse(dtoTeam.getBthdate()));
        return team;
    }
}
