package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/answers")
@CrossOrigin(origins = "http://localhost:5173")
public class Answer_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String ANSWER_WITH_THE_ID_X_NOT_FOUND = "Answer with the id %s not found!";

    AnswerDAO answerDAO;
    PollDAO pollDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        answerDAO = new AnswerDAO(em);
        pollDAO = new PollDAO(em);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity insert(@RequestBody Answer answer, HttpSession session){
        Long userId = null;
        Long deviceId = null;
        if(answer.getDevice() != null)deviceId = answer.getDevice().getId();
        Poll p = pollDAO.getPoll(answer.getPoll().getId());
        if(!p.getPublicPoll()){
            if(answer.get_user() != null)userId = answer.get_user().getId();
            long sessionUserId = -1;
            if(session.getAttribute("userId")!=null){
                sessionUserId = (long) session.getAttribute("userId");
            };
            if(sessionUserId == -1){
                return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
            }
            else{
                if(userId != sessionUserId){
                    return new ResponseEntity<>(new JSONObject().put("message","You can only answer for yourself").toString(), HttpStatus.UNAUTHORIZED);
                }
                else{
                    long id = answerDAO.newAnswer(answer.getColor(),userId,answer.getPoll().getId(),deviceId);
                    return new ResponseEntity<>(answerDAO.getAnswer(id), HttpStatus.OK);
                }

            }
        }
        else{
            long id = answerDAO.newAnswer(answer.getColor(),userId,answer.getPoll().getId(),deviceId);
            return new ResponseEntity<>(answerDAO.getAnswer(id), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity read(@PathVariable Long id){
        Answer a = answerDAO.getAnswer(id);
        if(a!=null){
            return new ResponseEntity<>(a, HttpStatus.OK);
        }
        else{
            System.out.println(ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new ResponseEntity<>(new JSONObject().put("message",ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = "application/json")
    public Object getAnswersByPoll(@RequestParam("pollId") long pollId){
        return new ResponseEntity<>(answerDAO.getAnswersByPoll(pollId), HttpStatus.OK);
    }

    /*@PutMapping(value = "/{id}", produces = "application/json")
    public Object update(@PathVariable Long id, @RequestBody Answer newAnswer){
        Answer a = answerDAO.getAnswer(id);
        if(a!=null){
            answerDAO.updateAnswer(id, newAnswer);
            return answerDAO.getAnswer(id);
        }
        else{
            System.out.println(ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }*/

    /*@DeleteMapping(value = "/{id}", produces = "application/json")
    public Object delete(@PathVariable Long id){
        Answer a = answerDAO.getAnswer(id);
        if(a!=null){
            answerDAO.deleteAnswer(id);
            return answerDAO.getAllAnswers();
        }
        else{
            System.out.println(ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }*/

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(answerDAO.getAllAnswers(), HttpStatus.OK);
    }
}
