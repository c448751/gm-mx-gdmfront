package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private VehicleMasterRepository repo;

    public List<VehicleMaster> search(
            String region,
            String brand,
            String type,
            String model,
            String year,
            String name
    ) {

        Integer regionId = parse(region);
        Integer brandId  = parse(brand);
        Integer typeId   = parse(type);
        Integer modelId  = parse(model);
        Integer yearVal  = parse(year);

        if (name != null && name.isBlank()) name = null;


        return repo.searchActive(regionId, brandId, typeId, modelId, yearVal, name);
    }

    private Integer parse(String val) {
        try {
            return (val == null || val.isBlank()) ? null : Integer.valueOf(val);
        } catch (Exception ex) {
            return null;
        }
    }
}
