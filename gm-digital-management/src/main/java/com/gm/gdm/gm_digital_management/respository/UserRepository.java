package com.gm.gdm.gm_digital_management.respository;

import com.gm.gdm.gm_digital_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Buscar por código de usuario (opcional)
    User findByCodUser(String codUser);
}