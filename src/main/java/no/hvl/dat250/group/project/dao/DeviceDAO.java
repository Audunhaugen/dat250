package no.hvl.dat250.group.project.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;

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
}
