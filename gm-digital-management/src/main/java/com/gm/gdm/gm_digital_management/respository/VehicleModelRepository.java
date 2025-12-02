package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleModelRepository extends JpaRepository<VehicleModel, Integer> {

    List<VehicleModel> findByBrand_IdBrand(Integer idBrand);
}
