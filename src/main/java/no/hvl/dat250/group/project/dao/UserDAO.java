package no.hvl.dat250.group.project.dao;

import jakarta.persistence.*;
import no.hvl.dat250.group.project._User;
import org.h2.engine.User;
import org.hibernate.HibernateException;

import java.util.List;


public class UserDAO {
    EntityManager em;
    public UserDAO(EntityManager em){
        this.em = em;
    }
    public Long registerUser(String userName, String firstName, String lastName, String password){
        em.getTransaction().begin();
        _User a = new _User();
        a.setUserName(userName);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setPassword(password);


        em.persist(a);
        em.getTransaction().commit();

        return a.getId();
    }

    public void updateUser(Long id, _User updatedUser){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        if(updatedUser.getUserName()!=null)a.setUserName(updatedUser.getUserName());
        if(updatedUser.getPassword()!=null)a.setPassword(updatedUser.getPassword());
        if(updatedUser.getAnswers()!=null)a.setAnswers(updatedUser.getAnswers());
        if(updatedUser.getPolls()!=null)a.setPolls(updatedUser.getPolls());
        if(updatedUser.getFirstName()!=null)a.setFirstName(updatedUser.getFirstName());
        if(updatedUser.getLastName()!=null)a.setLastName(updatedUser.getLastName());
        em.persist(a);
        em.getTransaction().commit();
    }

    public void deleteUser(Long id){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }
    public _User getUser(Long id){
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            _User a = em.find(_User.class, id);
            em.getTransaction().commit();
            return a;
        } catch (HibernateException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<_User> getAllUsers(){
        em.getTransaction().begin();
        String query = "SELECT u from _User u";
        Query q = em.createQuery(query, _User.class);
        List<_User> list = (List<_User>) q.getResultList();
        em.getTransaction().commit();
        return list;
    }
}
