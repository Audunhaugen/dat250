package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/polls")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class Poll_controller {

    public static final String POLL_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    static final String PERSISTENCE_UNIT_NAME = "group-project";

    PollDAO pollDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        pollDAO = new PollDAO(em);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity insert(@RequestBody Poll poll, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            System.out.println("Owner: " + poll.getOwner().getId() + " User: " + userId);
            if(poll.getOwner().getId() == userId){
                userId = pollDAO.newPoll(poll.getTitle(), poll.getDescription(), poll.getStatus(), poll.getPublicPoll(), poll.getOwner().getId());
                return new ResponseEntity<>(pollDAO.getPoll(userId), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new JSONObject().put("message","You can only make polls for you").toString(), HttpStatus.UNAUTHORIZED);
            }

        }

    }

    @GetMapping(produces = "application/json")
    public ResponseEntity pollsByUser(HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity<>(pollDAO.getPollsByUser(userId), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity read(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    return new ResponseEntity<>(p, HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only see your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }

    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Poll newPoll, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId()==userId){
                    pollDAO.updatePoll(id, newPoll);
                    return new ResponseEntity<>(pollDAO.getPoll(id), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only update your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }


    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    pollDAO.deletePoll(id);
                    return new ResponseEntity<>(pollDAO.getPollsByUser(userId), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only delete your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message", POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }


    }

    //@GetMapping
    //public List<Poll> getAll() {
        //return pollDAO.getAllPolls();
    //}
}
