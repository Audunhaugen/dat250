package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;

import java.time.LocalDateTime;
import java.util.List;


public class AnswerDAO {
    EntityManager em;
    public AnswerDAO(EntityManager em){
        this.em = em;
    }
    public Long newAnswer(int color, Long userId, Long pollId, Long deviceId){
        em.getTransaction().begin();
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
    }

    public void updateAnswer(Long id, Answer updatedAnswer){
        em.getTransaction().begin();
        Answer a = em.find(Answer.class, id);
        if(updatedAnswer.getColor()!=0)a.setColor(updatedAnswer.getColor());
        if(updatedAnswer.getPoll()!=null)a.setPoll(updatedAnswer.getPoll());
        if(updatedAnswer.getDevice()!=null)a.setDevice(updatedAnswer.getDevice());
        if(updatedAnswer.getTimeOfVote()!=null)a.setTimeOfVote(updatedAnswer.getTimeOfVote());
        if(updatedAnswer.get_user()!=null)a.set_user(updatedAnswer.get_user());
        em.persist(a);
        em.getTransaction().commit();
    }

    public void deleteAnswer(Long id){
        em.getTransaction().begin();
        Answer a = em.find(Answer.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }

    public Answer getAnswer(Long id){
        em.getTransaction().begin();
        Answer a = em.find(Answer.class, id);
        em.getTransaction().commit();
        return a;
    }

    public List<Answer> getAllAnswers(){
        em.getTransaction().begin();
        String query = "SELECT a from Answer a";
        Query q = em.createQuery(query, Answer.class);
        List<Answer> list = (List<Answer>) q.getResultList();
        em.getTransaction().commit();
        return list;
    }
}
