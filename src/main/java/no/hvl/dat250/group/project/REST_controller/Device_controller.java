package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.dao.AnswerDAO;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class Device_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    public Map<Long, Device> devices = new HashMap<>();

    public Long ids = 0L; //counter for ids, starts from 0

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

    @GetMapping("/{id}")
    public Device read(@PathVariable Long id){
        return deviceDAO.getDevice(id);
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

    @DeleteMapping("/{id}")
    public List<Device> delete(@PathVariable Long id){
        if (!devices.containsKey(id)){
            throw new RuntimeException(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        deviceDAO.deleteDevice(id);
        return deviceDAO.getAllDevices();
    }

    @GetMapping
    public List<Device> getAll() {
        return deviceDAO.getAllDevices();
    }
}
