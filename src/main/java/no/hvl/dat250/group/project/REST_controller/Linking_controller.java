package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.Poll;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.random.RandomGenerator;

import static java.lang.Thread.sleep;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
    public ResponseEntity createLink(@RequestParam("pollId") long pollId, HttpSession session){
        long userId = -1;
        if(session.getAttribute("userId")!=null){
            userId = (long) session.getAttribute("userId");
        };
        if(userId == -1){
            return new ResponseEntity<>(new JSONObject().put("message","You have to log in first at http://localhost:8080").toString(), HttpStatus.UNAUTHORIZED);
        }
        else{
            Poll p = pollDao.getPoll(pollId);
            if(p.getOwner().getId() == userId){
                int code=-1;
                while(true){
                    code = 1111;
                            //RandomGenerator.getDefault().nextInt(9999);
                    if(!codes.containsKey(code))break;
                }
                codes.put(code, pollId);
                int finalCode = code;
                runTimer(finalCode);
                return new ResponseEntity<>(new JSONObject().put("message", "Code generated: "+String.format("%04d", code)).toString(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new JSONObject().put("message", "You can only link your own polls").toString(), HttpStatus.UNAUTHORIZED);
            }

        }

    }
    @PostMapping(value = "/link", produces = "application/json")
    public ResponseEntity link(@RequestParam("code") int code){
        if(codes.containsKey(code)){
            long id = deviceDAO.newDevice(codes.get(code));
            return new ResponseEntity<>(deviceDAO.getDevice(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new JSONObject().put("message", "There is no poll with that code waiting to be linked").toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/delink/{id}", produces = "application/json")
    public ResponseEntity delink(@PathVariable("id") long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            deviceDAO.deleteDevice(id);
            return new ResponseEntity<>(deviceDAO.getAllDevices(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new JSONObject().put("message", "There is no device with that id").toString(), HttpStatus.NOT_FOUND);
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
