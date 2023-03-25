package main.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoPlayer {
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private String bthdate;
    private String position;
    private String team;
}
