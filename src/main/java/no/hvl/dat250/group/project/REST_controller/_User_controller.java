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
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class _User_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "User with the id %s not found!";

    UserDAO userDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        userDAO = new UserDAO(em);
    }

    /*@PostMapping(produces = "application/json")
    public ResponseEntity insert(@RequestBody _User user){
        long id = userDAO.registerUser(user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword());
        return new ResponseEntity<>(userDAO.getUser(id), HttpStatus.OK);
    }*/

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity read(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId") != null){
            userId = (long) session.getAttribute("userId");
            System.out.println("User ID " + userId);
        }
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            _User u = userDAO.getUser(id);
            if(u!=null){
                if(u.getId() == userId){
                    return new ResponseEntity<>(u, HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message", "You can only see your data").toString(), HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }

    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity update(@PathVariable Long id, @RequestBody _User newUser, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId") != null){
            userId = (long) session.getAttribute("userId");
        }
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            _User u = userDAO.getUser(id);
            if(u!=null){
                if(u.getId() == userId){
                    userDAO.updateUser(id, newUser);
                    return new ResponseEntity<>(userDAO.getUser(id), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message", "You can only update your data").toString(), HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId") != null){
            userId = (long) session.getAttribute("userId");
        }
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            _User u = userDAO.getUser(id);
            if(u!=null){
                if(u.getId() == userId){
                    userDAO.deleteUser(id);
                    return new ResponseEntity<>(userDAO.getAllUsers(), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message", "You can only delete your data").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message", USER_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }
    }
    /*@GetMapping
    public ResponseEntity getAll() {
        List<_User> b = userDAO.getAllUsers();
        return new ResponseEntity<>(userDAO.getAllUsers(), HttpStatus.OK);
    }*/
}
