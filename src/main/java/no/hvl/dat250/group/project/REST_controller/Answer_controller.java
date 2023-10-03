package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Answer;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Answer_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "Answer with the id %s not found!";

    public Long ids = 0L; //counter for ids, starts from 0

    AnswerDAO answerDAO;

    @PostConstruct
    public void initialize() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        answerDAO = new AnswerDAO(em);
    }

    @PostMapping
    public Answer insert(@RequestBody Answer answer){
        long id = answerDAO.newAnswer(answer.getColor(),answer.get_user().getId(),answer.getPoll().getId(),answer.getDevice().getId());
        return answerDAO.getAnswer(id);
    }

    @GetMapping("/{id}")
    public Answer read(@PathVariable Long id){
        return answerDAO.getAnswer(id);
    }
    @GetMapping
    public List<Answer> getAll() {
        return answerDAO.getAllAnswers();
    }
}
