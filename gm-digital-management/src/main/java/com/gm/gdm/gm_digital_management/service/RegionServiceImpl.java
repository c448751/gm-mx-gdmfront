package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.Region;

import com.gm.gdm.gm_digital_management.respository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    private final RegionRepository repository;

    public RegionServiceImpl(RegionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Region> findAll() {
        return repository.findAll();
    }
}