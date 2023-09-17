package no.hvl.dat250.group.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @Getter
    @Setter
    private Poll poll;
    @Getter
    @Setter
    private int greenVotes;
    @Getter
    @Setter
    private int redVotes;
}
