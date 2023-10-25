package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "http://localhost:5173")
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
    public Object login(@RequestParam("username") String username, @RequestParam("password") String password){
        List<_User> l = userDAO.getAllUsers();
        boolean ok = false;
        long id=-1;
        for(_User u : l){
            if(Objects.equals(u.getUserName(), u.getUserName())){
                if(Objects.equals(u.getPassword(), password)){
                    ok=true;
                    id=u.getId();
                }
                break;
            }
        }
        if(ok){
            return userDAO.getUser(id);
        }
        else{
            return new JSONObject().put("message", "Incorrect password").toString();
        }
    }
    @PostMapping(value = "/signup", produces = "application/json")
    public Object signup(@RequestBody _User user){
        List<_User> l = userDAO.getAllUsers();
        boolean exists = false;
        for(_User u : l){
            if(Objects.equals(u.getUserName(), u.getUserName())){
                exists=true;
                break;
            }
        }
        if(!exists){
            long id = userDAO.registerUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword());
            return userDAO.getUser(id);
        }
        else{
            return new JSONObject().put("message", "Username already exists").toString();
        }
    }
}

