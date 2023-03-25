package main.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Player {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate bthdate;
    private Position position;
    private Team team;
}
