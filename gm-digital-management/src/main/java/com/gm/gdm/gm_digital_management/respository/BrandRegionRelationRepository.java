package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.BrandRegionRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRegionRelationRepository extends JpaRepository<BrandRegionRelation, Integer> {

    // Para obtener marcas disponibles según región
    List<BrandRegionRelation> findByRegion_IdRegion(Integer idRegion);
}