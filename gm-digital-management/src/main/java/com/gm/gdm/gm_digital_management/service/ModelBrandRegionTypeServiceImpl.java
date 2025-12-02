package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.ModelBrandRegionTypeRelation;
import com.gm.gdm.gm_digital_management.respository.ModelBrandRegionTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelBrandRegionTypeServiceImpl implements ModelBrandRegionTypeService {

    private final ModelBrandRegionTypeRepository repository;

    public ModelBrandRegionTypeServiceImpl(ModelBrandRegionTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ModelBrandRegionTypeRelation> findByBrandRegion(Integer idBrandRegion) {
        return repository.findByBrandRegionRelation_Id(idBrandRegion);
    }

    @Override
    public List<ModelBrandRegionTypeRelation> findByModel(Integer idModel) {
        return repository.findByModel_IdModel(idModel);
    }
}
