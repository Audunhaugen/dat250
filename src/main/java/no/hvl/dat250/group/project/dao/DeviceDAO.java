package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;

import java.util.List;

public class DeviceDAO {
    EntityManager em;
    public DeviceDAO(EntityManager em){
        this.em = em;
    }
    public Long newDevice(Long pollId){
        Device a = new Device();

        Poll poll = em.find(Poll.class, pollId);
        a.setPoll(poll);
        poll.getDevices().add(a);
        em.persist(a);
        em.persist(poll);
        return a.getId();
    }


    public Device getDevice(Long id){
        em.getTransaction().begin();
        Device a = em.find(Device.class, id);
        em.getTransaction().commit();
        return a;
    }

    public List<Device> getAllDevices(){
        em.getTransaction().begin();
        String query = "SELECT * FROM device";
        Query q = em.createNativeQuery(query);
        em.getTransaction().commit();
        return (List<Device>) q.getResultList();
    }

    public void deleteDevice(Long id){
        em.getTransaction().begin();
        Device a = em.find(Device.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }
}
