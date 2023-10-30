package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class Login_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "User with the id %s not found!";

    UserDAO userDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        userDAO = new UserDAO(em);
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){
        List<_User> l = userDAO.getAllUsers();
        boolean ok = false;
        long id=-1;
        for(_User u : l){
            if(Objects.equals(u.getUserName(), username)){
                if(Objects.equals(u.getPassword(), password)){
                    ok=true;
                    id=u.getId();
                    session.setAttribute("userId", id);
                }
                break;
            }
        }
        if(ok){
            return new ResponseEntity<>(userDAO.getUser(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new JSONObject().put("message", "Incorrect password").toString(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity signup(@RequestBody _User user){
        List<_User> l = userDAO.getAllUsers();
        boolean exists = false;
        for(_User u : l){
            if(Objects.equals(u.getUserName(), user.getUserName())){
                exists=true;
                break;
            }
        }
        if(!exists){
            long id = userDAO.registerUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword());
            return new ResponseEntity<>(userDAO.getUser(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new JSONObject().put("message", "Username already exists").toString(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/logout", produces = "application/json")
    public ResponseEntity logout(HttpSession session){
        session.invalidate();
        return new ResponseEntity<>(new JSONObject().put("message", "Succesfully logged out").toString(), HttpStatus.OK);
    }
}

