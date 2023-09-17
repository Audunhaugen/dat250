package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project._User;
import org.h2.engine.User;


public class UserDAO {
    EntityManager em;
    public UserDAO(EntityManager em){
        this.em = em;
    }
    public Long registerUser(String userName, String firstName, String lastName, String password){
        _User a = new _User();
        a.setUserName(userName);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setPassword(password);


        em.persist(a);

        return a.getId();
    }

    public void updateUserName(Long id, String userName){
        _User a = em.find(_User.class, id);
        a.setUserName(userName);
        em.persist(a);
    }
    public void updateFirstName(Long id, String firstName){
        _User a = em.find(_User.class, id);
        a.setFirstName(firstName);
        em.persist(a);
    }
    public void updateLastName(Long id, String lastName){
        _User a = em.find(_User.class, id);
        a.setLastName(lastName);
        em.persist(a);
    }
    public void updatePassword(Long id, String password){
        _User a = em.find(_User.class, id);
        a.setPassword(password);
        em.persist(a);
    }
    public void deleteUser(Long id){
        _User a = em.find(_User.class, id);
        em.remove(a);
    }
}
