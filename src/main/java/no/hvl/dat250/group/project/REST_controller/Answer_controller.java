package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.json.JSONObject;
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

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        answerDAO = new AnswerDAO(em);
    }

    @PostMapping
    public Answer insert(@RequestBody Answer answer){
        Long userId = null;
        Long deviceId = null;
        if(answer.get_user() != null)userId = answer.get_user().getId();
        if(answer.getDevice() != null)deviceId = answer.getDevice().getId();
        long id = answerDAO.newAnswer(answer.getColor(),userId,answer.getPoll().getId(),deviceId);
        return answerDAO.getAnswer(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Object read(@PathVariable Long id){
        Answer a = answerDAO.getAnswer(id);
        if(a!=null){
            return a;
        }
        else{
            System.out.println(ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message",ANSWER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
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
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
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
    }

    @GetMapping
    public List<Answer> getAll() {
        return answerDAO.getAllAnswers();
    }
}
