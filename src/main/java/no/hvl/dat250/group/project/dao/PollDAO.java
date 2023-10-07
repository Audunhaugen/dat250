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
        em.getTransaction().begin();
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
        em.getTransaction().commit();
        return a.getId();
    }

    public void updatePoll(Long id, Poll updatedPoll){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        if(updatedPoll.getOwner()!=null)a.setOwner(updatedPoll.getOwner());
        if(updatedPoll.getTitle()!=null)a.setTitle(updatedPoll.getTitle());
        if(updatedPoll.getDescription()!=null)a.setDescription(updatedPoll.getDescription());
        if(updatedPoll.getStatus()!=null)a.setStatus(updatedPoll.getStatus());
        if(updatedPoll.getPublicPoll()!=null)a.setPublicPoll(updatedPoll.getPublicPoll());
        if(updatedPoll.getCreationTime()!=null)a.setCreationTime(updatedPoll.getCreationTime());
        if(updatedPoll.getAnswers()!=null)a.setAnswers(updatedPoll.getAnswers());
        if(updatedPoll.getDevices()!=null)a.setDevices(updatedPoll.getDevices());
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
        List<Poll> list = (List<Poll>) q.getResultList();
        em.getTransaction().commit();
        return list;
    }

    public void deletePoll(Long id){
        em.getTransaction().begin();
        Poll a = em.find(Poll.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }
}
