package no.hvl.dat250.group.project;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    public String userName;

    @Getter
    @Setter
    public String firstName;

    @Getter
    @Setter
    public String surName;

    @Getter
    @Setter
    public String password;

    @Getter
    @Setter
    @OneToMany
    public Set<Poll> polls;
}
