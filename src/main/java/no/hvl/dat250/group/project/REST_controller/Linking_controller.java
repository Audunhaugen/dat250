package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import static java.lang.Thread.sleep;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class Linking_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    DeviceDAO deviceDAO;
    PollDAO pollDao;

    HashMap<Integer, Long> codes = new HashMap<>();

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        deviceDAO = new DeviceDAO(em);
        pollDao = new PollDAO(em);
    }
    @GetMapping(value = "/link", produces = "application/json")
    public Object createLink(@RequestParam("pollId") long pollId, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new JSONObject().put("message","You have to log in first at http://localhost:8080").toString();
        }
        else{
            Poll p = pollDao.getPoll(pollId);
            if(p.getOwner().getId() == userId){
                int code=-1;
                while(true){
                    code = RandomGenerator.getDefault().nextInt(10000);
                    if(!codes.containsKey(code))break;
                }
                codes.put(code, pollId);
                int finalCode = code;
                runTimer(finalCode);
                return new JSONObject().put("message", "Code generated: "+code).toString();
            }
            else{
                return new JSONObject().put("message", "You can only link you own polls").toString();
            }

        }

    }
    @PostMapping(value = "/link", produces = "application/json")
    public Object link(@RequestParam("code") int code){
        if(codes.containsKey(code)){
            long id = deviceDAO.newDevice(codes.get(code));
            return deviceDAO.getDevice(id);
        }
        else{
            return new JSONObject().put("message", "There is no poll with that code waiting to be linked").toString();
        }
    }

    @GetMapping(value = "/delink/{id}", produces = "application/json")
    public Object delink(@PathVariable("id") long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            deviceDAO.deleteDevice(id);
            return deviceDAO.getAllDevices();
        }
        else{
            return new JSONObject().put("message", "There is no device with that id").toString();
        }
    }

    public void runTimer(int finalCode){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1000*60*5);
                    codes.remove(finalCode);
                    System.out.println("Code removed "+finalCode);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        t1.start();
    }
}
