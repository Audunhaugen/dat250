package no.hvl.dat250.group.project.dao;

import jakarta.persistence.*;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;
import org.hibernate.HibernateException;

import java.time.LocalDateTime;
import java.util.List;

public class PollDAO {
    EntityManager em;
    public PollDAO(EntityManager em){
        this.em = em;
    }
    public Long newPoll(String title, String description, Boolean status, Boolean publicPoll,Long userId){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
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
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void updatePoll(Long id, Poll updatedPoll){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
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
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public Poll getPoll(Long id){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Poll a = em.find(Poll.class, id);
            em.getTransaction().commit();
            return a;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public List<Poll> getAllPolls(){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            String query = "SELECT p from Poll p";
            Query q = em.createQuery(query, Poll.class);
            List<Poll> list = (List<Poll>) q.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public List<Poll> getPollsByUser(long userId){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            String query = "SELECT p from Poll p WHERE p.owner.id=?1";
            Query q = em.createQuery(query, Poll.class);
            q.setParameter(1, userId);
            List<Poll> list = (List<Poll>) q.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void deletePoll(Long id){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Poll a = em.find(Poll.class, id);
            em.remove(a);
            em.flush();
            em.clear();
            em.getTransaction().commit();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }
}
