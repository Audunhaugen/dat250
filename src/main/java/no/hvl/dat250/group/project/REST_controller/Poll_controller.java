package no.hvl.dat250.group.project.REST_controller;

import no.hvl.dat250.group.project.Poll;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/poll")
public class Poll_controller {

    public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    public Map<Long, Poll> polls = new HashMap<>();

    public Long ids = 0L; //counter for ids, starts from 0
    @PostMapping
    public Poll insert(@RequestBody Poll poll){
        poll.setId(ids++);
        polls.put(poll.getId(),poll);
        return poll;
    }

    @GetMapping("/{id}")
    public Poll read(@PathVariable Long id){
        if (!polls.containsKey(id)){
            throw new RuntimeException(TODO_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return polls.get(id);
    }

    @PutMapping("/{id}")
    public Poll update(@PathVariable Long id, @RequestBody Poll newPoll){
        if (!polls.containsKey(id)){
            throw new RuntimeException(TODO_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        Poll poll = polls.get(id);
        poll.setTitle(newPoll.getTitle());
        poll.setDescription(newPoll.getDescription());
        poll.setStatus(newPoll.getStatus());
        poll.setOwner(newPoll.getOwner());
        poll.setAnswers(newPoll.getAnswers());
        poll.setDevices(newPoll.getDevices());
        poll.setCreationTime(newPoll.getCreationTime());
        return poll;
    }

    @DeleteMapping("/{id}")
    public Poll delete(@PathVariable Long id){
        if (!polls.containsKey(id)){
            throw new RuntimeException(TODO_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return polls.remove(id);
    }

    @GetMapping
    public List<Poll> getAll() {
        return polls.values().stream().toList();
    }
}
