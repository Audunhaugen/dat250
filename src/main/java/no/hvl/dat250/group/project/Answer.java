package no.hvl.dat250.group.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
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
    @JsonIgnoreProperties({"polls","answers","password"})
    private _User _user;
    @ManyToOne
    @Getter
    @Setter
    @JsonIgnoreProperties({"owner","answers","devices"})
    private Poll poll;
    @ManyToOne
    @Getter
    @Setter
    @JsonIgnoreProperties({"poll","answers"})
    private Device device;
}
