package no.hvl.dat250.group.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @OneToMany(mappedBy = "device")
    @Getter
    @Setter
    @JsonIgnoreProperties({"_user","poll","device"})
    private Set<Answer> answers = new HashSet<>();

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnoreProperties({"owner","answers","devices"})
    private Poll poll;
}
