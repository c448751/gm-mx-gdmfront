package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.ModelBrandRegionTypeRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModelBrandRegionTypeRepository extends JpaRepository<ModelBrandRegionTypeRelation, Integer> {

    // Encontrar modelos disponibles por (REGION + BRAND)
    List<ModelBrandRegionTypeRelation> findByBrandRegionRelation_Id(Integer idBrandRegionRelation);

    // Encontrar tipos disponibles por (MODEL)
    List<ModelBrandRegionTypeRelation> findByModel_IdModel(Integer idModel);

    @Query("""
        SELECT r
        FROM ModelBrandRegionTypeRelation r
        WHERE r.model.idModel = :model
          AND r.type.idType = :type
          AND r.brandRegionRelation.id = (
                SELECT br.id
                FROM BrandRegionRelation br
                WHERE br.region.idRegion = :region
                  AND br.brand.idBrand = :brand
          )
    """)
    Optional<ModelBrandRegionTypeRelation> findByAllIds(
            @Param("region") Integer region,
            @Param("brand") Integer brand,
            @Param("type") Integer type,
            @Param("model") Integer model
    );
}
