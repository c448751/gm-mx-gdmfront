package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.BrandRegionRelation;

import java.util.List;

public interface BrandRegionRelationService {
    List<BrandRegionRelation> findByRegion(Integer idRegion);
}