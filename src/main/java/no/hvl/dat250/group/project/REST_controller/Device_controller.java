package no.hvl.dat250.group.project.REST_controller;

import no.hvl.dat250.group.project.Device;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class Device_controller {

    public static final String DEVICE_WITH_THE_ID_X_NOT_FOUND = "Poll with the id %s not found!";

    public Map<Long, Device> devices = new HashMap<>();

    public Long ids = 0L; //counter for ids, starts from 0
    @PostMapping
    public Device insert(@RequestBody Device device){
        device.setId(ids++);
        devices.put(device.getId(),device);
        return device;
    }

    @GetMapping("/{id}")
    public Device read(@PathVariable Long id){
        if (!devices.containsKey(id)){
            throw new RuntimeException(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return devices.get(id);
    }

    @PutMapping("/{id}")
    public Device update(@PathVariable Long id, @RequestBody Device newDevice){
        if (!devices.containsKey(id)){
            throw new RuntimeException(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        Device device = devices.get(id);
        device.setAnswers(newDevice.getAnswers());
        device.setPoll(newDevice.getPoll());
        return device;
    }

    @DeleteMapping("/{id}")
    public Device delete(@PathVariable Long id){
        if (!devices.containsKey(id)){
            throw new RuntimeException(DEVICE_WITH_THE_ID_X_NOT_FOUND.formatted(id));
        }
        return devices.remove(id);
    }

    @GetMapping
    public List<Device> getAll() {
        return devices.values().stream().toList();
    }
}
