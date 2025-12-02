package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.respository.ValidationRequestRepository;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleApiController {

    @Autowired
    private VehicleMasterRepository vehicleRepo;

    @Autowired
    private ValidationRequestRepository validationRepo;

    @GetMapping("/{id}")
    public Map<String,Object> getVehicle(@PathVariable Integer id) {

        VehicleMaster v = vehicleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Map<String,Object> json = new HashMap<>();

        json.put("brand",  v.getRelation().getBrandRegionRelation().getBrand().getNameBrand());
        json.put("type",   v.getRelation().getType().getTypeName());
        json.put("name",   v.getNameVehicle());
        json.put("model",  v.getRelation().getModel().getModelName());
        json.put("year",   v.getYear());
        json.put("region", v.getRelation().getBrandRegionRelation().getRegion().getNameRegion());

        // 👇 Verifica si ya existe solicitud PENDING DELETE
        boolean pendingDelete = validationRepo.existsPendingDelete(id);
        json.put("pendingDelete", pendingDelete);

        return json;
    }
}
