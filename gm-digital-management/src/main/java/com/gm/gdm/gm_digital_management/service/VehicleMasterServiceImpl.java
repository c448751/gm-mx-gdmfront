package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleMasterServiceImpl implements VehicleMasterService {

    private final VehicleMasterRepository repository;

    public VehicleMasterServiceImpl(VehicleMasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleMaster> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<VehicleMaster> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public VehicleMaster save(VehicleMaster vehicle) {
        return repository.save(vehicle);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}