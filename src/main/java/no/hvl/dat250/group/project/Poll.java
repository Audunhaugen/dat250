package no.hvl.dat250.group.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private LocalDateTime creationTime;

    @Getter
    @Setter
    private Boolean status; //open or closed

    @Getter
    @Setter
    private Boolean publicPoll;

    @ManyToOne
    @Getter
    @Setter
    private _User owner;

    @OneToMany(mappedBy = "poll")
    @Getter
    @Setter
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(mappedBy = "poll")
    @Getter
    @Setter
    private Set<Device> devices = new HashSet<>();
}

