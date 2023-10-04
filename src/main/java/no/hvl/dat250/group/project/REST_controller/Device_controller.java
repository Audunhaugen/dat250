package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class Device_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    DeviceDAO deviceDAO;

    @PostConstruct
    public void initialize() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
        deviceDAO = new DeviceDAO(em);
    }

    @PostMapping
    public Device insert(@RequestBody Device device){
        long id = deviceDAO.newDevice(device.getPoll().getId());
        return deviceDAO.getDevice(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Object read(@PathVariable Long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            return d;
        }
        else{
            System.out.println(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString();
        }
    }

    /*@PutMapping("/{id}")
    public Device update(@PathVariable Long id, @RequestBody Device newDevice){
        if (!devices.containsKey(id)){
            throw new RuntimeException(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        deviceDAO.
        device.setAnswers(newDevice.getAnswers());
        device.setPoll(newDevice.getPoll());

        return device;
    }*/

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Object delete(@PathVariable Long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            deviceDAO.deleteDevice(id);
            return deviceDAO.getAllDevices();
        }
        else{
            System.out.println(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new JSONObject().put("message", DEVICE_WITH_THE_ID_X_NOT_FOUND).toString();
        }
    }

    @GetMapping
    public List<Device> getAll() {
        return deviceDAO.getAllDevices();
    }
}
