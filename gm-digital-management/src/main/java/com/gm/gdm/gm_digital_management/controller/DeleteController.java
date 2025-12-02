package com.gm.gdm.gm_digital_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.models.ValidationRequest;
import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.respository.ValidationRequestRepository;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
import com.gm.gdm.gm_digital_management.service.ValidationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class DeleteController {

    @Autowired
    private VehicleMasterRepository vehicleRepo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ValidationRequestRepository validationRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/delete/confirm")
    public String confirmDelete(@RequestParam Integer idVehicle,
                                HttpSession session) throws Exception {

        User user = (User) session.getAttribute("USER_LOGGED");
        if (user == null)
            return "ERROR";

        VehicleMaster vehicle = vehicleRepo.findById(idVehicle).orElse(null);
        if (vehicle == null)
            return "ERROR";

        boolean existsPending = validationService.existsPendingDelete(idVehicle);
        if (existsPending)
            return "PENDING_EXISTS";

        ValidationRequest req = new ValidationRequest();
        req.setAction("DELETE");
        req.setAttribute("VEHICLE_DELETE");

        Map<String, Object> beforeMap = new HashMap<>();
        beforeMap.put("id", vehicle.getIdVehicle());
        beforeMap.put("nameVehicle", vehicle.getNameVehicle());
        beforeMap.put("year", vehicle.getYear());
        beforeMap.put("brand", vehicle.getRelation().getBrandRegionRelation().getBrand().getNameBrand());
        beforeMap.put("region", vehicle.getRelation().getBrandRegionRelation().getRegion().getNameRegion());
        beforeMap.put("model", vehicle.getRelation().getModel().getModelName());
        beforeMap.put("type", vehicle.getRelation().getType().getTypeName());
        beforeMap.put("fileNameEng", vehicle.getFileNameEng());
        beforeMap.put("fileNameSpa", vehicle.getFileNameSpa());

        req.setValueBefore(mapper.writeValueAsString(beforeMap));
        req.setValueAfter("{}");

        req.setUserRequest(user);
        req.setVehicle(vehicle);
        req.setStatus("PENDING");
        req.setCreatedAt(LocalDateTime.now());

        validationRepo.save(req);

        return "OK";
    }
}
