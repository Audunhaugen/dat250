package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/poll")
public class Poll_controller {

    public static final String POLL_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public Map<Long, Poll> polls = new HashMap<>();

    public Long ids = 0L; //counter for ids, starts from 0

    PollDAO pollDAO;

    @PostConstruct
    public void initialize() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        pollDAO = new PollDAO(em);
    }

    @PostMapping
    public Poll insert(@RequestBody Poll poll){
        long id = pollDAO.newPoll(poll.getTitle(), poll.getDescription(), poll.getStatus(), poll.getPublicPoll(), poll.getOwner().getId());
        poll.setId(ids++);
        polls.put(poll.getId(),poll);
        return pollDAO.getPoll(id);
    }

    @GetMapping("/{id}")
    public Poll read(@PathVariable Long id){
        if (!polls.containsKey(id)){
            throw new RuntimeException(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return pollDAO.getPoll(id);
    }

    @PutMapping("/{id}")
    public Poll update(@PathVariable Long id, @RequestBody Poll newPoll){
        if (!polls.containsKey(id)){
            throw new RuntimeException(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        pollDAO.updateCreationTime(id, newPoll.getCreationTime());
        pollDAO.updatePollTitle(id, newPoll.getTitle());
        pollDAO.updatePublicPoll(id, newPoll.getPublicPoll());
        pollDAO.updateStatus(id, newPoll.getStatus());
        pollDAO.updateDescription(id, newPoll.getDescription());

        return pollDAO.getPoll(id);
    }

    @DeleteMapping("/{id}")
    public List<Poll> delete(@PathVariable Long id){
        if (!polls.containsKey(id)){
            throw new RuntimeException(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        pollDAO.deletePoll(id);
        return pollDAO.getAllPolls();
    }

    @GetMapping
    public List<Poll> getAll() {
        return pollDAO.getAllPolls();
    }
}
