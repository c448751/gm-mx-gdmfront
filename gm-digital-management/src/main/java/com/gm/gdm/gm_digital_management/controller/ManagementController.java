package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.dto.VehicleFormDTO;
import com.gm.gdm.gm_digital_management.models.*;
import com.gm.gdm.gm_digital_management.respository.*;
import com.gm.gdm.gm_digital_management.service.ValidationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ManagementController {

    @Autowired private RegionRepository regionRepo;
    @Autowired private BrandRepository brandRepo;
    @Autowired private VehicleTypeRepository typeRepo;
    @Autowired private VehicleModelRepository modelRepo;
    @Autowired private ModelBrandRegionTypeRepository relationRepo;
    @Autowired private VehicleMasterRepository vehicleMasterRepo;
    @Autowired private ValidationService validationService;

    private final String pdfPath = "C:/gdm/pdf/";

    // ====================================================
    //              MANAGEMENT PAGE
    // ====================================================
    @GetMapping("/management")
    public String managementPage(
            @RequestParam(required = false) Integer region,
            @RequestParam(required = false) Integer brand,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer modelId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String name,
            Model model
    ) {

        model.addAttribute("regions", regionRepo.findAll());
        model.addAttribute("brands", brandRepo.findAll());
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("models", modelRepo.findAll());

        // 🔥 SOLO VEHÍCULOS ACTIVOS
        var vehicles = vehicleMasterRepo.searchActive(region, brand, type, modelId, year, name);
        model.addAttribute("vehicles", vehicles);

        return "management/management";
    }


    // ====================================================
    //                ADD FORM
    // ====================================================
    @GetMapping("/management/add")
    public String addVehicleForm(Model model) {

        model.addAttribute("regions", regionRepo.findAll());
        model.addAttribute("brands", brandRepo.findAll());
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("models", modelRepo.findAll());

        model.addAttribute("vehicleForm", new VehicleFormDTO());
        return "management/management-add";
    }

    // ====================================================
    //            SAVE NEW VEHICLE
    // ====================================================
    @PostMapping("/management/save")
    public String saveVehicle(
            @ModelAttribute("vehicleForm") VehicleFormDTO form,
            @RequestParam("fileEng") MultipartFile fileEng,
            @RequestParam("fileSpa") MultipartFile fileSpa,
            HttpSession session,
            Model model
    ) throws Exception {

        User loggedUser = (User) session.getAttribute("USER_LOGGED");
        if (loggedUser == null)
            throw new RuntimeException("User not logged in");

        model.addAttribute("regions", regionRepo.findAll());
        model.addAttribute("brands", brandRepo.findAll());
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("models", modelRepo.findAll());

        if (!fileEng.isEmpty() && !fileEng.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            model.addAttribute("invalidPdfEng", true);
            return "management/management-add";
        }

        if (!fileSpa.isEmpty() && !fileSpa.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            model.addAttribute("invalidPdfSpa", true);
            return "management/management-add";
        }

        boolean existsInMaster =
                vehicleMasterRepo.existsVehicleWithSpecs(
                        form.getRegionId(), form.getBrandId(),
                        form.getTypeId(), form.getModelId(),
                        form.getYear(), form.getNameVehicle()
                );

        boolean existsPending =
                validationService.existsPendingAdd(form.getNameVehicle(), form.getYear());

        if (existsInMaster || existsPending) {
            model.addAttribute("vehicleExists", true);
            return "management/management-add";
        }

        var relation = relationRepo.findByAllIds(
                form.getRegionId(), form.getBrandId(),
                form.getTypeId(), form.getModelId()
        ).orElseThrow(() -> new RuntimeException("Invalid combination"));

        VehicleMaster after = new VehicleMaster();
        after.setRelation(relation);
        after.setNameVehicle(form.getNameVehicle());
        after.setYear(form.getYear());

        String baseName = sanitize(
                relation.getBrandRegionRelation().getBrand().getNameBrand() + "_" +
                        relation.getModel().getModelName() + "_" +
                        form.getYear()
        );

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm"));

        if (!fileEng.isEmpty()) {
            String fileNameEng = baseName + "_ENG_" + timestamp + ".pdf";
            saveFile(fileEng, fileNameEng);
            after.setFileNameEng(fileNameEng);
        }

        if (!fileSpa.isEmpty()) {
            String fileNameSpa = baseName + "_SPA_" + timestamp + ".pdf";
            saveFile(fileSpa, fileNameSpa);
            after.setFileNameSpa(fileNameSpa);
        }

        validationService.createAddVehicleRequest(after, loggedUser);

        model.addAttribute("requestSubmitted", true);
        model.addAttribute("vehicleForm", new VehicleFormDTO());

        return "management/management-add";
    }

    // ====================================================
    // UTILIDADES
    // ====================================================
    private void saveFile(MultipartFile file, String fileName) throws IOException {
        File folder = new File(pdfPath);
        if (!folder.exists()) folder.mkdirs();
        File dest = new File(folder, fileName);
        file.transferTo(dest);
    }

    private String sanitize(String text) {
        return text.toUpperCase()
                .replace(" ", "_")
                .replaceAll("[^A-Z0-9_]", "");
    }
}
