package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;

import java.time.LocalDateTime;

public class PollDAO {
    EntityManager em;
    public PollDAO(EntityManager em){
        this.em = em;
    }
    public Long newPoll(String title, String description, Boolean status,Long userId){
       Poll a = new Poll();
       a.setTitle(title);
       a.setDescription(description);
       a.setStatus(status);
       a.setCreationTime(LocalDateTime.now());

        _User user = em.find(_User.class, userId);
        a.setOwner(user);
        user.getPolls().add(a);
        em.persist(a);
        em.persist(user);
        return a.getId();
    }
}
