package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.Brand;
import com.gm.gdm.gm_digital_management.respository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository repository;

    public BrandServiceImpl(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Brand> findAll() {
        return repository.findAll();
    }
}