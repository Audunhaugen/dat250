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
        em.getTransaction().begin();
        Device a = new Device();

        Poll poll = em.find(Poll.class, pollId);
        a.setPoll(poll);
        poll.getDevices().add(a);
        em.persist(a);
        em.persist(poll);
        em.getTransaction().commit();
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
        String query = "SELECT d from Device d";
        Query q = em.createQuery(query, Device.class);
        List<Device> list = (List<Device>) q.getResultList();
        em.getTransaction().commit();
        return list;
    }

    public void deleteDevice(Long id){
        em.getTransaction().begin();
        Device a = em.find(Device.class, id);
        em.remove(a);
        em.getTransaction().commit();
    }

    public void updateDevice(Long id, Device updatedDevice){
        em.getTransaction().begin();
        Device a = em.find(Device.class, id);
        if(updatedDevice.getPoll()!=null)a.setPoll(updatedDevice.getPoll());
        if(updatedDevice.getAnswers()!=null)a.setAnswers(updatedDevice.getAnswers());
        em.persist(a);
        em.getTransaction().commit();
    }
}
