package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.service.BrandService;
import com.gm.gdm.gm_digital_management.service.RegionService;
import com.gm.gdm.gm_digital_management.service.VehicleModelService;
import com.gm.gdm.gm_digital_management.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    private final RegionService regionService;
    private final BrandService brandService;
    private final VehicleTypeService vehicleTypeService;
    private final VehicleModelService vehicleModelService;

    public HomeController(RegionService regionService,
                          BrandService brandService,
                          VehicleTypeService vehicleTypeService,
                          VehicleModelService vehicleModelService) {

        this.regionService = regionService;
        this.brandService = brandService;
        this.vehicleTypeService = vehicleTypeService;
        this.vehicleModelService = vehicleModelService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("regiones", regionService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("types", vehicleTypeService.findAll());
        model.addAttribute("models", vehicleModelService.findAll());

        return "home/main"; // ← templates/home/home.html
    }
}
