package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.VehicleMaster;

import java.util.List;
import java.util.Optional;

public interface VehicleMasterService {
    List<VehicleMaster> findAll();
    Optional<VehicleMaster> findById(Integer id);
    VehicleMaster save(VehicleMaster vehicle);
    void delete(Integer id);
}