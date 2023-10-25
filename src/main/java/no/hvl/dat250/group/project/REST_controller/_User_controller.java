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
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class _User_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "User with the id %s not found!";

    UserDAO userDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        userDAO = new UserDAO(em);
    }

    @PostMapping
    public Object insert(@RequestBody _User user){
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
            return new JSONObject().put("message", "Username already exists");
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Object read(@PathVariable Long id){
        _User u = userDAO.getUser(id);
        if(u!=null){
            return u;
        }
        else{
            System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public Object update(@PathVariable Long id, @RequestBody _User newUser){
        _User u = userDAO.getUser(id);
        if(u!=null){
            userDAO.updateUser(id, newUser);
            return userDAO.getUser(id);
        }
        else{
            System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Object delete(@PathVariable Long id){
        _User u = userDAO.getUser(id);
        if(u!=null){
            userDAO.deleteUser(id);
            return userDAO.getAllUsers();
        }
        else{
            System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }
    @GetMapping
    public List<_User> getAll() {
        List<_User> b = userDAO.getAllUsers();
        return userDAO.getAllUsers();
    }
}
