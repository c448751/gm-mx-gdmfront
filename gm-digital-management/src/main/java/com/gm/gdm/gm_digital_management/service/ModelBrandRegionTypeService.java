package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.ModelBrandRegionTypeRelation;

import java.util.List;

public interface ModelBrandRegionTypeService {
    List<ModelBrandRegionTypeRelation> findByBrandRegion(Integer idBrandRegion);
    List<ModelBrandRegionTypeRelation> findByModel(Integer idModel);
}