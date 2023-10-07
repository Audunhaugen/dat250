package no.hvl.dat250.group.project;

import com.fasterxml.jackson.annotation.*;
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
    @Setter
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
    @JsonIgnoreProperties({"polls","answers"})
    private _User owner;

    @OneToMany(mappedBy = "poll")
    @Getter
    @Setter
    @JsonIgnoreProperties({"poll","_user","device"})
    private Set<Answer> answers = new HashSet<>();

    @OneToMany(mappedBy = "poll")
    @Getter
    @Setter
    @JsonIgnoreProperties({"poll","answers"})
    private Set<Device> devices = new HashSet<>();
}

