package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.ValidationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ValidationRequestRepository extends JpaRepository<ValidationRequest, Integer> {

    @Query("""
        SELECT COUNT(v) > 0
        FROM ValidationRequest v
        WHERE v.vehicle.idVehicle = :vehicleId
        AND v.status = 'PENDING'
    """)
    boolean existsPendingRequest(@Param("vehicleId") Integer vehicleId);

    @Query("SELECT v FROM ValidationRequest v WHERE v.status = 'PENDING'")
    List<ValidationRequest> findAllPending();

    @Query("""
        SELECT v
        FROM ValidationRequest v
        WHERE v.status = 'PENDING'
          AND v.action = 'ADD'
          AND v.attribute = 'VEHICLE_ADD'
    """)
    List<ValidationRequest> findAllPendingVehicleAdd();

    @Query("""
        SELECT COUNT(v) > 0
        FROM ValidationRequest v
        WHERE v.status   = 'PENDING'
          AND v.action   = 'ADD'
          AND v.attribute = 'VEHICLE_ADD'
          AND v.vehicle IS NULL
          AND UPPER(v.valueAfter) LIKE CONCAT('%\"nameVehicle\":\"', UPPER(:name), '\"%')
          AND v.valueAfter LIKE CONCAT('%\"year\":', :year, '%')
    """)
    boolean existsPendingAddByNameAndYear(
            @Param("name") String name,
            @Param("year") Integer year
    );


    @Query("""
        SELECT v
        FROM ValidationRequest v
        WHERE v.status IN ('APPROVED', 'REJECTED')
        ORDER BY v.approvalDate DESC
    """)
    List<ValidationRequest> findHistory();

    @Query("""
    SELECT COUNT(v) > 0
    FROM ValidationRequest v
    WHERE v.vehicle.idVehicle = :vehicleId
      AND v.status = 'PENDING'
      AND v.action = 'DELETE'
""")
    boolean existsPendingDelete(@Param("vehicleId") Integer vehicleId);


}
