package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project._User;
import no.hvl.dat250.group.project.dao.UserDAO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/users")
public class _User_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    public Map<Long, _User> users = new HashMap<>();
    public Long ids = 0L; //counter for ids, starts from 0

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
        user.setId(id);
        users.put(user.getId(),user);
        return userDAO.getUser(id);
    }

    @GetMapping("/{id}")
    public _User read(@PathVariable Long id){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return userDAO.getUser(id);
    }

    @PutMapping("/{id}")
    public _User update(@PathVariable Long id, @RequestBody _User newUser){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        userDAO.updatePassword(id, newUser.getPassword());
        userDAO.updateUserName(id, newUser.getUserName());
        userDAO.updateFirstName(id, newUser.getFirstName());
        userDAO.updateLastName(id, newUser.getLastName());
        return userDAO.getUser(id);
    }

    @DeleteMapping("/{id}")
    public List<_User> delete(@PathVariable Long id){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        userDAO.deleteUser(id);
        return userDAO.getAllUsers();
    }
    @GetMapping
    public List<_User> getAll() {
        return userDAO.getAllUsers();
    }
}
