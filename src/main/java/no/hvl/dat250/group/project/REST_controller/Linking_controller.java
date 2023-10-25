package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import no.hvl.dat250.group.project.dao.PollDAO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

@RestController
@RequestMapping("/polls/link")
@CrossOrigin(origins = "http://localhost:5173")
public class Linking_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    DeviceDAO deviceDAO;
    PollDAO pollDao;

    HashMap<Integer, Long> codes;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        deviceDAO = new DeviceDAO(em);
        pollDao = new PollDAO(em);
    }
    @GetMapping()
    public Object createLink(@RequestParam("pollId") long pollId){
        int code=-1;
        while(true){
            code = RandomGenerator.getDefault().nextInt(10000);
            if(!codes.containsKey(code))break;
        }
        codes.put(code, pollId);
        return new JSONObject().put("message", "Code generated").toString();
    }
    @PostMapping()
    public Object link(@RequestParam("code") int code){
        if(codes.containsKey(code)){
            long id = deviceDAO.newDevice(codes.get(code));
            return deviceDAO.getDevice(id);
        }
        else{
            return new JSONObject().put("message", "There is no poll with that code waiting to be linked").toString();
        }
    }
}
