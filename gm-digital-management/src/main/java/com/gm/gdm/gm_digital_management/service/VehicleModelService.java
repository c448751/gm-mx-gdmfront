package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.VehicleModel;

import java.util.List;

public interface VehicleModelService {
    List<VehicleModel> findAll();
    List<VehicleModel> findByBrand(Integer idBrand);
}