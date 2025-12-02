package com.gm.gdm.gm_digital_management.config;

import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private UserRepository userRepo;

    // Mapa para convertir ID_REGN_FK → Región en texto
    private static final Map<Integer, String> REGION_MAP = Map.of(
            1, "MEXICO",
            2, "USA",
            3, "GLOBAL"
    );

    @ModelAttribute("userName")
    public String getUserName() {

        // ⚠️ TEMPORAL → usar ID = 1
        User user = userRepo.findById(1).orElse(null);

        return user != null ? user.getNomUser() : "USER";
    }

    @ModelAttribute("userRegion")
    public String getUserRegion() {

        User user = userRepo.findById(1).orElse(null);

        if (user == null) return "UNKNOWN";

        return REGION_MAP.getOrDefault(user.getRegionUser(), "UNKNOWN");
    }
}