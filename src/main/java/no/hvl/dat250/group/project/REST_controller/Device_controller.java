package no.hvl.dat250.group.project.REST_controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import no.hvl.dat250.group.project.Device;
import no.hvl.dat250.group.project.dao.DeviceDAO;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
@CrossOrigin(origins = "http://localhost:5173")
public class Device_controller {
    static final String PERSISTENCE_UNIT_NAME = "group-project";

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    DeviceDAO deviceDAO;

    @PostConstruct
    public void initialize() {
        EntityManager em = RestServiceApplication.getEntityManager();
        deviceDAO = new DeviceDAO(em);
    }

    @PostMapping
    public ResponseEntity insert(@RequestBody Device device){
        long id = deviceDAO.newDevice(device.getPoll().getId());
        return new ResponseEntity<>(deviceDAO.getDevice(id), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity read(@PathVariable Long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            return new ResponseEntity<>(d, HttpStatus.OK);
        }
        else{
            System.out.println(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new ResponseEntity<>(new JSONObject().put("message", DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id)).toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Device newDevice){
        Device d = deviceDAO.getDevice(id);
        if(d != null){
            deviceDAO.updateDevice(id, newDevice);
            return new ResponseEntity<>(deviceDAO.getDevice(id), HttpStatus.OK);
        }
        else{
            System.out.println(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new ResponseEntity<>(new JSONObject().put("message", DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id)), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable Long id){
        Device d = deviceDAO.getDevice(id);
        if(d!=null){
            deviceDAO.deleteDevice(id);
            return new ResponseEntity<>(deviceDAO.getAllDevices(), HttpStatus.OK);
        }
        else{
            System.out.println(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
            return new ResponseEntity<>(new JSONObject().put("message", DEVICE_WITH_THE_ID_X_NOT_FOUND).toString(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(deviceDAO.getAllDevices(), HttpStatus.OK);
    }
}
