package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
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
public class EditController {

    @Autowired
    private VehicleMasterRepository vehicleMasterRepo;

    @Autowired
    private ValidationService validationService;

    private final String pdfPath = "C:/gdm/pdf/";

    // ===========================
    //   MOSTRAR PANTALLA EDIT
    // ===========================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {

        VehicleMaster vehicle = vehicleMasterRepo.findFullById(id);

        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }

        model.addAttribute("vehicle", vehicle);
        return "home/edit";
    }

    // ===========================
    //   GUARDAR CAMBIOS / REQUEST
    // ===========================
    @PostMapping("/edit/save")
    public String save(
            @ModelAttribute("vehicle") VehicleMaster formVehicle,
            @RequestParam("fileEng") MultipartFile fileEng,
            @RequestParam("fileSpa") MultipartFile fileSpa,
            HttpSession session,
            Model model
    ) throws Exception {

        User loggedUser = (User) session.getAttribute("USER_LOGGED");
        if (loggedUser == null) {
            throw new RuntimeException("User not logged in");
        }

        Integer idVehicle = formVehicle.getIdVehicle();

        // =============================================
        // 🔥 VALIDAR SI YA EXISTE UNA SOLICITUD PENDIENTE
        // =============================================
        if (validationService.hasPendingRequest(idVehicle)) {
            model.addAttribute("pendingRequest", true);
            model.addAttribute("vehicle", vehicleMasterRepo.findFullById(idVehicle));
            return "home/edit";
        }

        // ======== BEFORE ORIGINAL ========
        VehicleMaster before = vehicleMasterRepo.findFullById(idVehicle);

        boolean changed = false;

        if (!before.getNameVehicle().equals(formVehicle.getNameVehicle()))
            changed = true;

        if (!before.getYear().equals(formVehicle.getYear()))
            changed = true;

        if (!fileEng.isEmpty())
            changed = true;

        if (!fileSpa.isEmpty())
            changed = true;

        // =============================================
        // 🔥 SI NO HAY CAMBIOS
        // =============================================
        if (!changed) {
            model.addAttribute("noChanges", true);
            model.addAttribute("vehicle", before);
            return "home/edit";
        }

        // ======== AFTER ========
        VehicleMaster after = new VehicleMaster();
        after.setIdVehicle(before.getIdVehicle());
        after.setRelation(before.getRelation());
        after.setNameVehicle(formVehicle.getNameVehicle());
        after.setYear(formVehicle.getYear());
        after.setFileNameEng(before.getFileNameEng());
        after.setFileNameSpa(before.getFileNameSpa());

        // ======== ARCHIVOS ========
        String brand = before.getRelation().getModel().getBrand().getNameBrand();
        String modelName = before.getRelation().getModel().getModelName();
        Integer year = formVehicle.getYear();

        String baseName = sanitize(brand + "_" + modelName + "_" + year);

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

        // Crear solicitud de validación
        validationService.createValidationRequest(before, after, loggedUser, "VEHICLE_EDIT");

        model.addAttribute("requestSubmitted", true);
        model.addAttribute("vehicle", before);
        return "home/edit";
    }

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
