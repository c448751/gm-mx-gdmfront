package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleMasterRepository extends JpaRepository<VehicleMaster, Integer> {

    @Query("""
        SELECT vm
        FROM VehicleMaster vm
        WHERE (:region IS NULL OR vm.relation.brandRegionRelation.region.idRegion = :region)
        AND (:brand IS NULL OR vm.relation.brandRegionRelation.brand.idBrand = :brand)
        AND (:type IS NULL OR vm.relation.type.idType = :type)
        AND (:model IS NULL OR vm.relation.model.idModel = :model)
        AND (:year IS NULL OR vm.year = :year)
        AND (:name IS NULL OR LOWER(vm.nameVehicle) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    List<VehicleMaster> search(
            @Param("region") Integer region,
            @Param("brand") Integer brand,
            @Param("type") Integer type,
            @Param("model") Integer model,
            @Param("year") Integer year,
            @Param("name") String name
    );

    @Query("""
        SELECT vm
        FROM VehicleMaster vm
        JOIN FETCH vm.relation r
        JOIN FETCH r.brandRegionRelation brr
        JOIN FETCH brr.region reg
        JOIN FETCH r.model m
        JOIN FETCH r.type t
        WHERE vm.idVehicle = :id
    """)
    VehicleMaster findFullById(@Param("id") Integer id);
    @Query("""
        SELECT COUNT(vm) > 0
        FROM VehicleMaster vm
        WHERE vm.relation.brandRegionRelation.region.idRegion = :region
          AND vm.relation.brandRegionRelation.brand.idBrand = :brand
          AND vm.relation.type.idType = :type
          AND vm.relation.model.idModel = :model
          AND vm.year = :year
          AND UPPER(vm.nameVehicle) = UPPER(:name)
    """)
    boolean existsVehicleWithSpecs(@Param("region") Integer region,
                                   @Param("brand") Integer brand,
                                   @Param("type") Integer type,
                                   @Param("model") Integer model,
                                   @Param("year") Integer year,
                                   @Param("name") String name);

    @Query("""
        SELECT COUNT(vm) > 0
        FROM VehicleMaster vm
        WHERE vm.relation.brandRegionRelation.region.idRegion = :regionId
          AND vm.relation.brandRegionRelation.brand.idBrand   = :brandId
          AND vm.relation.type.idType                         = :typeId
          AND vm.relation.model.idModel                       = :modelId
          AND vm.year                                         = :year
          AND UPPER(vm.nameVehicle) = UPPER(:name)
    """)
    boolean existsBySpec(
            @Param("regionId") Integer regionId,
            @Param("brandId") Integer brandId,
            @Param("typeId") Integer typeId,
            @Param("modelId") Integer modelId,
            @Param("year") Integer year,
            @Param("name") String name
    );
    @Query("""
    SELECT v FROM VehicleMaster v
    WHERE v.status = 'ACTIVE'
      AND (:region IS NULL OR v.relation.brandRegionRelation.region.idRegion = :region)
      AND (:brand IS NULL OR v.relation.brandRegionRelation.brand.idBrand = :brand)
      AND (:type IS NULL OR v.relation.type.idType = :type)
      AND (:model IS NULL OR v.relation.model.idModel = :model)
      AND (:year IS NULL OR v.year = :year)
      AND (:name IS NULL OR UPPER(v.nameVehicle) LIKE CONCAT('%', UPPER(:name), '%'))
""")
    List<VehicleMaster> searchActive(
            @Param("region") Integer region,
            @Param("brand") Integer brand,
            @Param("type") Integer type,
            @Param("model") Integer model,
            @Param("year") Integer year,
            @Param("name") String name
    );

}
