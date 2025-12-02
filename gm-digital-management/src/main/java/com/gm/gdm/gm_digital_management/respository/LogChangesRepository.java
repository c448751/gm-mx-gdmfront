package com.gm.gdm.gm_digital_management.respository;


import com.gm.gdm.gm_digital_management.models.LogChanges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogChangesRepository extends JpaRepository<LogChanges, Integer> {
}