package no.hvl.dat250.group.project.dao;

import jakarta.persistence.*;
import no.hvl.dat250.group.project.*;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public class AnswerDAO {
    EntityManager em;

    public AnswerDAO(EntityManager em){
        this.em = em;
    }
    public Long newAnswer(int color, Long userId, Long pollId, Long deviceId){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Answer a = new Answer();
            a.setColor(color);
            a.setTimeOfVote(LocalDateTime.now());
            _User user=new _User();
            Poll poll;
            Device device= new Device();

            if(userId!=null){
                user = em.find(_User.class, userId);
                a.set_user(user);
                user.getAnswers().add(a);
            }
            poll = em.find(Poll.class, pollId);
            a.setPoll(poll);
            poll.getAnswers().add(a);
            if(deviceId!=null){
                device = em.find(Device.class, deviceId);
                a.setDevice(device);
                device.getAnswers().add(a);
            }
            em.persist(a);
            if(userId!=null)em.persist(user);
            em.persist(poll);
            if(deviceId!=null)em.persist(device);
            em.getTransaction().commit();
            return a.getId();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void updateAnswer(Long id, Answer updatedAnswer){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Answer a = em.find(Answer.class, id);
            if(updatedAnswer.getColor()!=0)a.setColor(updatedAnswer.getColor());
            if(updatedAnswer.getPoll()!=null){//solve this by obtaining poll from em and updating with that and poll with answer
                a.setPoll(updatedAnswer.getPoll());

            }
            if(updatedAnswer.getDevice()!=null)a.setDevice(updatedAnswer.getDevice());
            if(updatedAnswer.getTimeOfVote()!=null)a.setTimeOfVote(updatedAnswer.getTimeOfVote());
            if(updatedAnswer.get_user()!=null)a.set_user(updatedAnswer.get_user());
            em.persist(a);
            em.getTransaction().commit();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void deleteAnswer(Long id){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Answer a = em.find(Answer.class, id);
            em.remove(a);
            em.getTransaction().commit();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public Answer getAnswer(Long id){
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Answer a = em.find(Answer.class, id);
            em.getTransaction().commit();
            return a;
        } catch (HibernateException e){
            if( transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public List<Answer> getAnswersByPoll(long pollId){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            String query = "SELECT a from Answer a WHERE a.poll.id=?1";
            Query q = em.createQuery(query, Answer.class);
            q.setParameter(1, pollId);
            List<Answer> list = (List<Answer>) q.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }


    }

    public List<Answer> getAllAnswers(){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            String query = "SELECT a from Answer a";
            Query q = em.createQuery(query, Answer.class);
            List<Answer> list = (List<Answer>) q.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }
}
