package com.gm.gdm.gm_digital_management.config;


import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.models.Region;
import com.gm.gdm.gm_digital_management.service.RegionService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserAttributes {

    @Autowired
    private RegionService regionService;

    @ModelAttribute
    public void addGlobalUser(Model model, HttpSession session) {

        User user = (User) session.getAttribute("USER_LOGGED");

        if (user != null) {
            // Nombre del usuario que elegiste en el FakeUserFilter
            model.addAttribute("userName", user.getNomUser());

            // Obtener nombre real de la región
            Region region = regionService.findAll()
                    .stream()
                    .filter(r -> r.getIdRegion().equals(user.getRegionUser()))
                    .findFirst()
                    .orElse(null);

            String regionName = (region != null) ? region.getNameRegion() : "SIN REGIÓN";
            model.addAttribute("userRegion", regionName);
        }
    }
}
