package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/polls")
public class Poll_controller {

    public static final String POLL_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    static final String PERSISTENCE_UNIT_NAME = "group-project";

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
        return pollDAO.getPoll(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Object read(@PathVariable Long id){
        Poll p = pollDAO.getPoll(id);
        if(p != null){
            return p;
        }
        else{
            System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public Object update(@PathVariable Long id, @RequestBody Poll newPoll){
        Poll p = pollDAO.getPoll(id);
        if(p != null){
            pollDAO.updatePoll(id, newPoll);
            return pollDAO.getPoll(id);
        }
        else{
            System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Object delete(@PathVariable Long id){
        Poll p = pollDAO.getPoll(id);
        if(p != null){
            pollDAO.deletePoll(id);
            return pollDAO.getAllPolls();
        }
        else{
            System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
    }

    @GetMapping
    public List<Poll> getAll() {
        return pollDAO.getAllPolls();
    }
}
