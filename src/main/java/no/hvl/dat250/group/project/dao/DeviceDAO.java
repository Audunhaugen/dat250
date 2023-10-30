package no.hvl.dat250.group.project.dao;

import jakarta.persistence.*;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;
import org.hibernate.HibernateException;

import java.util.List;

public class DeviceDAO {
    EntityManager em;
    public DeviceDAO(EntityManager em){
        this.em = em;
    }
    public Long newDevice(Long pollId){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Device a = new Device();

            Poll poll = em.find(Poll.class, pollId);
            a.setPoll(poll);
            poll.getDevices().add(a);
            em.persist(a);
            em.persist(poll);
            em.getTransaction().commit();
            return a.getId();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }


    public Device getDevice(Long id){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Device a = em.find(Device.class, id);
            em.getTransaction().commit();
            return a;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public List<Device> getAllDevices(){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            String query = "SELECT d from Device d";
            Query q = em.createQuery(query, Device.class);
            List<Device> list = (List<Device>) q.getResultList();
            em.getTransaction().commit();
            return list;
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void deleteDevice(Long id){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Device a = em.find(Device.class, id);
            em.remove(a);
            em.getTransaction().commit();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }

    public void updateDevice(Long id, Device updatedDevice){
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Device a = em.find(Device.class, id);
            if(updatedDevice.getPoll()!=null)a.setPoll(updatedDevice.getPoll());
            if(updatedDevice.getAnswers()!=null)a.setAnswers(updatedDevice.getAnswers());
            em.persist(a);
            em.getTransaction().commit();
        } catch (HibernateException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }

    }
}
