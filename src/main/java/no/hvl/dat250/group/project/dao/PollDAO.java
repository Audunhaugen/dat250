package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;

import java.time.LocalDateTime;
import java.util.List;

public class PollDAO {
    EntityManager em;
    public PollDAO(EntityManager em){
        this.em = em;
    }
    public Long newPoll(String title, String description, Boolean status, Boolean publicPoll,Long userId){
       Poll a = new Poll();
       a.setTitle(title);
       a.setDescription(description);
       a.setStatus(status);
       a.setCreationTime(LocalDateTime.now());
       a.setPublicPoll(publicPoll);

        _User user = em.find(_User.class, userId);
        a.setOwner(user);
        user.getPolls().add(a);
        em.persist(a);
        em.persist(user);
        return a.getId();
    }

    public void updatePollTitle(Long id, String pollTitle){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        a.setTitle(pollTitle);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updateDescription(Long id, String description){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        a.setDescription(description);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updateStatus(Long id, Boolean status){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        a.setStatus(status);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updateCreationTime(Long id, LocalDateTime creationTime){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        a.setCreationTime(creationTime);
        em.persist(a);
        em.getTransaction().commit();
    }

    public void updatePublicPoll(Long id, Boolean publicPoll){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        a.setPublicPoll(publicPoll);
        em.persist(a);
        em.getTransaction().commit();
    }


    public Poll getPoll(Long id){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        em.getTransaction().commit();
        return a;
    }

    public List<Poll> getAllPolls(){
        em.getTransaction().begin();
        String query = "SELECT p from Poll p";
        Query q = em.createQuery(query, Poll.class);
        em.getTransaction().commit();
        return (List<Poll>) q.getResultList();
    }

    public void deletePoll(Long id){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }
}
