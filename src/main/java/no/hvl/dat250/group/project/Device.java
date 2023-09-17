package no.hvl.dat250.group.project;

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
    private Long id;
    @OneToMany(mappedBy = "device")
    @Getter
    @Setter
    private Set<Answer> answers = new HashSet<>();
    @Getter
    @Setter
    @ManyToOne
    private Poll poll;
}
