package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.VehicleType;
import com.gm.gdm.gm_digital_management.respository.VehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository repository;

    public VehicleTypeServiceImpl(VehicleTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleType> findAll() {
        return repository.findAll();
    }
}
