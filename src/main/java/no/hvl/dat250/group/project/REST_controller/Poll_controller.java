package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping("/polls")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class Poll_controller {

    public static final String POLL_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    static final String PERSISTENCE_UNIT_NAME = "group-project";

    PollDAO pollDAO;
    HashMap<String, Long> links = new HashMap<>();

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        pollDAO = new PollDAO(em);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity insert(@RequestBody Poll poll, HttpSession session) throws URISyntaxException, IOException, InterruptedException {
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            System.out.println("Owner: " + poll.getOwner().getId() + " User: " + userId);
            if(poll.getOwner().getId() == userId){
                Long pollId = pollDAO.newPoll(poll.getTitle(), poll.getDescription(), poll.getStatus(), poll.getPublicPoll(), poll.getOwner().getId());
                publishToDweet(poll.getTitle(), true);
                return new ResponseEntity<>(pollDAO.getPoll(pollId), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new JSONObject().put("message","You can only make polls for you").toString(), HttpStatus.UNAUTHORIZED);
            }

        }

    }

    private void publishToDweet(String pollTitle, boolean openOrClosed) throws URISyntaxException, IOException, InterruptedException {
        JSONObject o = new JSONObject();
        o.put("pollTitle", pollTitle);
        o.put("status", openOrClosed ? "open" : "closed");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://dweet.io/dweet/for/poll")).header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(o.toString())).build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

    }

    @GetMapping(produces = "application/json")
    public ResponseEntity pollsByUser(HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            return new ResponseEntity<>(pollDAO.getPollsByUser(userId), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity read(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    return new ResponseEntity<>(p, HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only see your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }

    }

    @GetMapping(value = "/vote/{link}", produces = "application/json")
    public ResponseEntity readForVoting(@PathVariable String link){
        if(links.containsKey(link)){
            long pId = links.get(link);
            Poll p = pollDAO.getPoll(pId);
            if(p != null){
                return new ResponseEntity<>(p, HttpStatus.OK);

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(pId));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(pId)).toString(), HttpStatus.NOT_FOUND);
            }
        }
        else{
            return new ResponseEntity<>(new JSONObject().put("message","Invalid link").toString(), HttpStatus.NOT_FOUND);
        }


    }
    @GetMapping(value = "/share/{id}", produces = "application/json")
    public ResponseEntity sharePoll(@PathVariable Long id, HttpSession session ){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        }
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    String link = getAlphaNumericString(10);
                    links.put(link, p.getId());
                    runTimer(link);
                    JSONObject o = new JSONObject();
                    o.put("link", link);
                    return new ResponseEntity<>(o.toString(), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only share your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Poll newPoll, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId()==userId){
                    pollDAO.updatePoll(id, newPoll);
                    return new ResponseEntity<>(pollDAO.getPoll(id), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only update your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }


    }

    @GetMapping(value = "{id}/close", produces = "application/json")
    public ResponseEntity closePoll(@PathVariable Long id, HttpSession session) throws URISyntaxException, IOException, InterruptedException {
        long userId = -1;
        if(session.getAttribute("userId") != null){
            userId = (long) session.getAttribute("userId");
        }
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    p.setStatus(false);
                    pollDAO.updatePoll(id, p);
                    publishToDweet(p.getTitle(), false);
                    return new ResponseEntity<>(pollDAO.getPoll(id), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only close your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message",POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable Long id, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDAO.getPoll(id);
            if(p != null){
                if(p.getOwner().getId() == userId){
                    pollDAO.deletePoll(id);
                    return new ResponseEntity<>(pollDAO.getPollsByUser(userId), HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>(new JSONObject().put("message","You can only delete your polls").toString(), HttpStatus.UNAUTHORIZED);
                }

            }
            else{
                System.out.println(POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id));
                return new ResponseEntity<>(new JSONObject().put("message", POLL_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
            }
        }


    }


    private String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public void runTimer(String link){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1*60*60*1000);
                    links.remove(link);
                    System.out.println("link removed "+link);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        t1.start();
    }


    //@GetMapping
    //public List<Poll> getAll() {
        //return pollDAO.getAllPolls();
    //}
}
