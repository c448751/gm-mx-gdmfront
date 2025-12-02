package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.BrandRegionRelation;
import com.gm.gdm.gm_digital_management.respository.BrandRegionRelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandRegionRelationServiceImpl implements BrandRegionRelationService {

    private final BrandRegionRelationRepository repository;

    public BrandRegionRelationServiceImpl(BrandRegionRelationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BrandRegionRelation> findByRegion(Integer idRegion) {
        return repository.findByRegion_IdRegion(idRegion);
    }
}