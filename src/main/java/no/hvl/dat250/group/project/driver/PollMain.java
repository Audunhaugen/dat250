package no.hvl.dat250.group.project.driver;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.*;
import no.hvl.dat250.group.project.dao.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.Set;

public class PollMain {

  static final String PERSISTENCE_UNIT_NAME = "group-project";

  public static void main(String[] args) {
    try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
        PERSISTENCE_UNIT_NAME); EntityManager em = factory.createEntityManager()) {
      em.getTransaction().begin();
      createObjects(em);
      em.getTransaction().commit();
      em.getTransaction().begin();
      createObjectsDAO(em);
      em.getTransaction().commit();
    }

  }

  private static void createObjects(EntityManager em) {
    _User creator_user = new _User();
    creator_user.setUserName("CreatorUser");
    creator_user.setFirstName("Creator");
    creator_user.setLastName("User");
    creator_user.setPassword("password");

    Poll poll = new Poll();
    poll.setTitle("TestPoll");
    poll.setDescription("This is a test poll");
    poll.setCreationTime(LocalDateTime.now());
    poll.setStatus(true);
    poll.setPublicPoll(true);

    poll.setOwner(creator_user);
    creator_user.getPolls().add(poll);

    _User answer_user = new _User();
    answer_user.setUserName("AnswerUser");
    answer_user.setFirstName("Answer");
    answer_user.setLastName("User");
    answer_user.setPassword("password");

    Answer answer = new Answer();
    answer.setColor(0);
    answer.setTimeOfVote(LocalDateTime.now());
    answer.set_user(answer_user);
    answer.setPoll(poll);

    answer_user.getAnswers().add(answer);
    poll.getAnswers().add(answer);

    Device device = new Device();
    device.setPoll(poll);
    poll.getDevices().add(device);

    Answer anonynousAnswer = new Answer();
    anonynousAnswer.setColor(1);
    anonynousAnswer.setTimeOfVote(LocalDateTime.now());
    anonynousAnswer.setPoll(poll);
    poll.getAnswers().add(anonynousAnswer);
    anonynousAnswer.setDevice(device);
    device.getAnswers().add(anonynousAnswer);

    em.persist(creator_user);
    em.persist(poll);
    em.persist(answer_user);
    em.persist(answer);
    em.persist(device);
    em.persist(anonynousAnswer);
  }

  private static void createObjectsDAO(EntityManager em){
    AnswerDAO aDAO = new AnswerDAO(em);
    DeviceDAO dDAO = new DeviceDAO(em);
    PollDAO pDAO = new PollDAO(em);
    UserDAO uDAO = new UserDAO(em);


    Long userId = uDAO.registerUser("username", "Larry", "Wheels", "12345678");
    //uDAO.updatePassword(userId, "SecretPassword");

    Long pollId = pDAO.newPoll("DAO created Poll", "testing",true,true,userId);
    Long deviceId = dDAO.newDevice(pollId);

    Long answerId = aDAO.newAnswer(1, userId, pollId, null);
    Long answerId2 = aDAO.newAnswer(0,null,pollId, deviceId);
  }
}
