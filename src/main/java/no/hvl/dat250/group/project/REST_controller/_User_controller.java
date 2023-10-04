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



@RestController
@RequestMapping("/users")
public class _User_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "User with the id %s not found!";

    UserDAO userDAO;

    @PostConstruct
    public void initialize() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        userDAO = new UserDAO(em);
    }

    @PostMapping
    public _User insert(@RequestBody _User user){
        long id = userDAO.registerUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword());
        return userDAO.getUser(id);
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
            userDAO.updatePassword(id, newUser.getPassword());
            userDAO.updateUserName(id, newUser.getUserName());
            userDAO.updateFirstName(id, newUser.getFirstName());
            userDAO.updateLastName(id, newUser.getLastName());
            return u;
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
        return userDAO.getAllUsers();
    }
}
