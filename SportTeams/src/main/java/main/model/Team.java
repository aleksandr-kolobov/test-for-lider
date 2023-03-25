package main.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Team {
    private int id;
    private String name;
    private Sport sport;
    private LocalDate bthdate;
}
