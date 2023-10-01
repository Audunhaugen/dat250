package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import no.hvl.dat250.group.project._User;
import org.h2.engine.User;

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

    public void updateUserName(Long id, String userName){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        a.setUserName(userName);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updateFirstName(Long id, String firstName){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        a.setFirstName(firstName);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updateLastName(Long id, String lastName){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        a.setLastName(lastName);
        em.persist(a);
        em.getTransaction().commit();
    }
    public void updatePassword(Long id, String password){
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        a.setPassword(password);
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
        em.getTransaction().begin();
        _User a = em.find(_User.class, id);
        em.getTransaction().commit();
        return a;
    }

    public List<_User> getAllUsers(){
        em.getTransaction().begin();
        String query = "SELECT * FROM _user";
        Query q = em.createNativeQuery(query);
        em.getTransaction().commit();
        return (List<_User>) q.getResultList();
    }
}
