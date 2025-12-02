package com.gm.gdm.gm_digital_management.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.gdm.gm_digital_management.models.*;
import com.gm.gdm.gm_digital_management.respository.ModelBrandRegionTypeRepository;
import com.gm.gdm.gm_digital_management.respository.ValidationRequestRepository;
import com.gm.gdm.gm_digital_management.respository.VehicleMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    @Autowired
    private ValidationRequestRepository validationRepo;

    @Autowired
    private VehicleMasterRepository vehicleRepo;

    @Autowired
    private ModelBrandRegionTypeRepository relationRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    // ==========================================
    // EXISTE SOLICITUD PENDIENTE
    // ==========================================
    public boolean existsPendingRequest(Integer vehicleId) {
        return validationRepo.existsPendingRequest(vehicleId);
    }

    public boolean hasPendingRequest(Integer vehicleId) {
        return validationRepo.existsPendingRequest(vehicleId);
    }

    // ==========================================
    // 🔵 NUEVO: EXISTE SOLICITUD PENDIENTE DELETE
    // ==========================================
    public boolean existsPendingDelete(Integer vehicleId) {
        return validationRepo.existsPendingDelete(vehicleId);
    }

    // ==========================================
    // CREAR SOLICITUD DE EDICIÓN
    // ==========================================
    public void createValidationRequest(
            VehicleMaster before,
            VehicleMaster after,
            User currentUser,
            String attributeName
    ) throws Exception {

        ValidationRequest req = new ValidationRequest();
        req.setAction("EDIT");
        req.setAttribute(attributeName);

        Map<String, Object> beforeMap = new HashMap<>();
        beforeMap.put("nameVehicle", before.getNameVehicle());
        beforeMap.put("year", before.getYear());
        beforeMap.put("fileNameEng", before.getFileNameEng());
        beforeMap.put("fileNameSpa", before.getFileNameSpa());

        Map<String, Object> afterMap = new HashMap<>();
        afterMap.put("nameVehicle", after.getNameVehicle());
        afterMap.put("year", after.getYear());
        afterMap.put("fileNameEng", after.getFileNameEng());
        afterMap.put("fileNameSpa", after.getFileNameSpa());

        req.setValueBefore(mapper.writeValueAsString(beforeMap));
        req.setValueAfter(mapper.writeValueAsString(afterMap));

        req.setUserRequest(currentUser);
        req.setStatus("PENDING");
        req.setVehicle(before);
        req.setCreatedAt(LocalDateTime.now());

        validationRepo.save(req);
    }

    // ==========================================
    // LISTAR PENDIENTES
    // ==========================================
    public List<ValidationRequest> getPendingValidations() {
        return validationRepo.findAllPending();
    }

    // ==========================================

    public void approveRequest(Integer idRequest, User approver) throws Exception {

        ValidationRequest req = validationRepo.findById(idRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Request already processed");

        // REGLA: NO PUEDE APROBAR SU PROPIA SOLICITUD
        if (req.getUserRequest().getIdUser().equals(approver.getIdUser()))
            throw new RuntimeException("You cannot approve your own request.");

        String action = req.getAction() != null ? req.getAction().toUpperCase() : "";
        boolean isAdd    = "ADD".equals(action);
        boolean isEdit   = "EDIT".equals(action);
        boolean isDelete = "DELETE".equals(action);

        // ============================
        // CASO 1 — ADD
        // ============================
        if (isAdd) {

            Map<String, Object> afterMap =
                    mapper.readValue(req.getValueAfter(), Map.class);

            String name = (String) afterMap.get("nameVehicle");
            Integer year = (Integer) afterMap.get("year");
            Integer region = (Integer) afterMap.get("region");
            Integer brand = (Integer) afterMap.get("brand");
            Integer type = (Integer) afterMap.get("type");
            Integer model = (Integer) afterMap.get("model");

            // Se debe buscar el relation REAL en BD
            ModelBrandRegionTypeRelation relation =
                    relationRepo.findByAllIds(region, brand, type, model)
                            .orElseThrow(() -> new RuntimeException("Relation not found"));

            VehicleMaster newVehicle = new VehicleMaster();
            newVehicle.setNameVehicle(name);
            newVehicle.setYear(year);
            newVehicle.setRelation(relation);

            // archivos podrían venir nulos
            if (afterMap.containsKey("fileNameEng"))
                newVehicle.setFileNameEng((String) afterMap.get("fileNameEng"));

            if (afterMap.containsKey("fileNameSpa"))
                newVehicle.setFileNameSpa((String) afterMap.get("fileNameSpa"));

            // Si tienes un campo de status/activo, puedes inicializarlo aquí como ACTIVO
            // newVehicle.setStatus("ACTIVE");

            vehicleRepo.save(newVehicle);

            req.setVehicle(newVehicle);
        }

        // ============================
        // CASO 2 — EDIT
        // ============================
        else if (isEdit) {

            Map<String, Object> afterMap =
                    mapper.readValue(req.getValueAfter(), Map.class);

            VehicleMaster vehicle = vehicleRepo.findById(
                    req.getVehicle().getIdVehicle()
            ).orElseThrow(() -> new RuntimeException("Vehicle not found"));

            vehicle.setNameVehicle((String) afterMap.get("nameVehicle"));
            vehicle.setYear((Integer) afterMap.get("year"));
            vehicle.setFileNameEng((String) afterMap.get("fileNameEng"));
            vehicle.setFileNameSpa((String) afterMap.get("fileNameSpa"));

            vehicleRepo.save(vehicle);
        }

        // ============================
        // CASO 3 — DELETE (BORRADO LÓGICO)
        // ============================
        else if (isDelete) {

            VehicleMaster vehicle = vehicleRepo.findById(
                    req.getVehicle().getIdVehicle()
            ).orElseThrow(() -> new RuntimeException("Vehicle not found"));

            // BORRADO LÓGICO
            // Ajusta esta línea según el campo real que tengas en VehicleMaster:
            //  - setStatus("INACTIVE")
            //  - setFlgStats("I")
            //  - setActive(false)
            vehicle.setStatus("INACTIVE");  // TODO: Ajustar al nombre real del campo

            vehicleRepo.save(vehicle);
        }

        // ============================
        // ACTUALIZAR REQUEST
        // ============================
        req.setStatus("APPROVED");
        req.setUserApprover(approver);
        req.setApprovalDate(LocalDateTime.now());
        validationRepo.save(req);
    }

    // ==========================================
    // RECHAZAR
    // ==========================================
    public void rejectRequest(Integer idRequest, User approver) {

        ValidationRequest req = validationRepo.findById(idRequest)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Request already processed");


        req.setStatus("REJECTED");
        req.setUserApprover(approver);
        req.setApprovalDate(LocalDateTime.now());

        validationRepo.save(req);
    }

    public ValidationRequest getById(Integer id) {
        return validationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    // ==========================================
    // CREAR SOLICITUD ADD
    // ==========================================
    public void createAddVehicleRequest(
            VehicleMaster after,
            User currentUser
    ) throws Exception {

        ValidationRequest req = new ValidationRequest();
        req.setAction("ADD");
        req.setAttribute("VEHICLE_ADD");

        Map<String, Object> map = new HashMap<>();
        map.put("nameVehicle", after.getNameVehicle());
        map.put("year", after.getYear());
        map.put("fileNameEng", after.getFileNameEng());
        map.put("fileNameSpa", after.getFileNameSpa());

        // RELATION DEBE ENVIARSE POR ID
        map.put("region", after.getRelation().getBrandRegionRelation().getRegion().getIdRegion());
        map.put("brand", after.getRelation().getBrandRegionRelation().getBrand().getIdBrand());
        map.put("type", after.getRelation().getType().getIdType());
        map.put("model", after.getRelation().getModel().getIdModel());

        req.setValueAfter(mapper.writeValueAsString(map));

        req.setUserRequest(currentUser);
        req.setStatus("PENDING");
        req.setCreatedAt(LocalDateTime.now());
        req.setVehicle(null);

        validationRepo.save(req);
    }

    public boolean existsPendingAdd(String nameVehicle, Integer year) {
        return validationRepo.existsPendingAddByNameAndYear(nameVehicle, year);
    }

    // ==========================================
    // 🔵 LISTAR HISTORIAL (YA EXISTENTE)
    // ==========================================
    public List<ValidationRequest> getHistory() {
        return validationRepo.findHistory();
    }

    // ==========================================
    // 🔵 NUEVO: CREAR SOLICITUD DELETE
    // ==========================================
    public void createDeleteVehicleRequest(
            VehicleMaster vehicle,
            User currentUser
    ) throws Exception {

        ValidationRequest req = new ValidationRequest();
        req.setAction("DELETE");
        req.setAttribute("VEHICLE_DELETE");

        // BEFORE = información actual del vehículo
        Map<String, Object> before = new HashMap<>();
        before.put("idVehicle", vehicle.getIdVehicle());
        before.put("nameVehicle", vehicle.getNameVehicle());
        before.put("year", vehicle.getYear());
        before.put("fileNameEng", vehicle.getFileNameEng());
        before.put("fileNameSpa", vehicle.getFileNameSpa());

        if (vehicle.getRelation() != null
                && vehicle.getRelation().getBrandRegionRelation() != null) {

            before.put("region", vehicle.getRelation()
                    .getBrandRegionRelation().getRegion().getNameRegion());

            before.put("brand", vehicle.getRelation()
                    .getBrandRegionRelation().getBrand().getNameBrand());

            before.put("type", vehicle.getRelation()
                    .getType().getTypeName());

            before.put("model", vehicle.getRelation() 
                    .getModel().getModelName());
        }

        req.setValueBefore(mapper.writeValueAsString(before));

        // AFTER lo dejamos como una marca de intención, no se usará para cambios de campo
        Map<String, Object> after = new HashMap<>();
        after.put("deleted", true);
        req.setValueAfter(mapper.writeValueAsString(after));

        req.setUserRequest(currentUser);
        req.setStatus("PENDING");
        req.setCreatedAt(LocalDateTime.now());

        // MUY IMPORTANTE: ligamos el vehículo a la request
        req.setVehicle(vehicle);

        validationRepo.save(req);
    }
}
