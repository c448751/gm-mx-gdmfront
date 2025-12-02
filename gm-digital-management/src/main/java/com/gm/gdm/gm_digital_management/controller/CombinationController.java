package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.models.Brand;
import com.gm.gdm.gm_digital_management.models.BrandRegionRelation;
import com.gm.gdm.gm_digital_management.models.ModelBrandRegionTypeRelation;
import com.gm.gdm.gm_digital_management.models.Region;
import com.gm.gdm.gm_digital_management.models.VehicleModel;
import com.gm.gdm.gm_digital_management.models.VehicleType;
import com.gm.gdm.gm_digital_management.respository.BrandRegionRelationRepository;
import com.gm.gdm.gm_digital_management.respository.BrandRepository;
import com.gm.gdm.gm_digital_management.respository.ModelBrandRegionTypeRepository;
import com.gm.gdm.gm_digital_management.respository.RegionRepository;
import com.gm.gdm.gm_digital_management.respository.VehicleModelRepository;
import com.gm.gdm.gm_digital_management.respository.VehicleTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CombinationController {

    @Autowired private RegionRepository regionRepo;
    @Autowired private BrandRepository brandRepo;
    @Autowired private VehicleModelRepository modelRepo;
    @Autowired private VehicleTypeRepository typeRepo;
    @Autowired private BrandRegionRelationRepository brandRegionRepo;
    @Autowired private ModelBrandRegionTypeRepository mbrtRepo;

    // =====================================================
    //     PANTALLA PRINCIPAL DE COMBINACIONES
    // =====================================================
    @GetMapping("/management/combinations")
    public String combinationsPage(Model model) {

        model.addAttribute("regions", regionRepo.findAll());
        model.addAttribute("brands", brandRepo.findAll());
        model.addAttribute("models", modelRepo.findAll());
        model.addAttribute("types", typeRepo.findAll());

        model.addAttribute("combinations", mbrtRepo.findAll());

        return "management/combinations";
    }

    // =====================================================
    //            GUARDAR NUEVA COMBINACIÓN
    // =====================================================
    @PostMapping("/management/combinations/save")
    public String saveCombination(
            @RequestParam Integer regionId,
            @RequestParam Integer brandId,
            @RequestParam Integer modelId,
            @RequestParam Integer typeId,
            Model model
    ) {

        // 1) Obtener brand-region
        BrandRegionRelation brandRegion = brandRegionRepo.findByRegion_IdRegion(regionId)
                .stream()
                .filter(br -> br.getBrand().getIdBrand().equals(brandId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Brand-Region relation not found"));

        // 2) Obtener model y type
        VehicleModel vm = modelRepo.findById(modelId)
                .orElseThrow(() -> new RuntimeException("Model not found"));

        VehicleType type = typeRepo.findById(typeId)
                .orElseThrow(() -> new RuntimeException("Type not found"));

        // 3) Crear relación completa
        ModelBrandRegionTypeRelation rel = new ModelBrandRegionTypeRelation();
        rel.setBrandRegionRelation(brandRegion);
        rel.setModel(vm);
        rel.setType(type);

        // 4) Guardar
        mbrtRepo.save(rel);

        return "redirect:/management/combinations";
    }
}
