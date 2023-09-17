package no.hvl.dat250.group.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private int color;

    @Getter
    @Setter
    private LocalDateTime timeOfVote;
    @ManyToOne
    @Getter
    @Setter
    private _User _user;
    @ManyToOne
    @Setter
    private Poll poll;
    @ManyToOne
    @Setter
    private Device device;
}
