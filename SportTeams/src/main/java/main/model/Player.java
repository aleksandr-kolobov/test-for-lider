package main.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate bthdate;
    @Enumerated(EnumType.STRING)
    private Position position;
    private int teamId;
}
