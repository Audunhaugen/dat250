package no.hvl.dat250.group.project.REST_controller;

import no.hvl.dat250.group.project._User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class _User_controller {
    public static final String USER_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    public Map<Long, _User> users = new HashMap<>();

    public Long ids = 0L; //counter for ids, starts from 0

    @PostMapping
    public _User insert(@RequestBody _User user){
        user.setId(ids++);
        users.put(user.getId(),user);
        return user;
    }

    @GetMapping("/{id}")
    public _User read(@PathVariable Long id){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return users.get(id);
    }

    @PutMapping("/{id}")
    public _User update(@PathVariable Long id, @RequestBody _User newUser){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        _User user = users.get(id);
        user.setUserName(newUser.getUserName());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPassword(newUser.getPassword());
        user.setPolls(newUser.getPolls());
        user.setAnswers(newUser.getAnswers());
        return user;
    }

    @DeleteMapping("/{id}")
    public _User delete(@PathVariable Long id){
        if (!users.containsKey(id)){
            throw new RuntimeException(USER_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return users.remove(id);
    }
    @GetMapping
    public List<_User> getAll() {
        return users.values().stream().toList();
    }
}
