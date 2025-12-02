package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.VehicleModel;
import com.gm.gdm.gm_digital_management.respository.VehicleModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository repository;

    public VehicleModelServiceImpl(VehicleModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleModel> findAll() {
        return repository.findAll();
    }

    @Override
    public List<VehicleModel> findByBrand(Integer idBrand) {
        return repository.findByBrand_IdBrand(idBrand);
    }
}
