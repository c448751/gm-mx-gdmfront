package com.gm.gdm.gm_digital_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gm.gdm.gm_digital_management.dto.ChangeItem;
import com.gm.gdm.gm_digital_management.models.User;
import com.gm.gdm.gm_digital_management.models.ValidationRequest;
import com.gm.gdm.gm_digital_management.models.VehicleMaster;
import com.gm.gdm.gm_digital_management.service.ValidationService;
import com.gm.gdm.gm_digital_management.respository.ValidationRequestRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MonitoringController {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ValidationRequestRepository validationRepo;

    // ==========================================================
    //          MONITORING PAGE
    // ==========================================================
    @GetMapping("/monitoring")
    public String monitoring(Model model) {


        List<ValidationRequest> pending = validationService.getPendingValidations();
        model.addAttribute("requests", pending);


        List<ValidationRequest> history = validationService.getHistory();
        model.addAttribute("history", history);

        return "monitoring/monitoring";
    }

    // ==========================================================
    //      DETAILS - AJAX JSON
    // ==========================================================
    @GetMapping("/monitoring/details/{id}")
    @ResponseBody
    public Map<String, Object> getDetails(@PathVariable Integer id) throws Exception {

        ValidationRequest req = validationService.getById(id);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> before = new HashMap<>();
        Map<String, Object> after = new HashMap<>();

        String vb = req.getValueBefore();
        String va = req.getValueAfter();

        if (vb != null && !vb.isBlank()) {
            try {
                before = mapper.readValue(vb, Map.class);
            } catch (Exception ignored) {}
        }

        if (va != null && !va.isBlank()) {
            try {
                after = mapper.readValue(va, Map.class);
            } catch (Exception ignored) {}
        }

        boolean isCreate = req.getAction() != null &&
                (req.getAction().equalsIgnoreCase("ADD")
                        || req.getAction().equalsIgnoreCase("CREATE"));

        List<ChangeItem> changes = new ArrayList<>();

        // Llaves combinadas
        Set<String> keys = new HashSet<>();
        keys.addAll(before.keySet());
        keys.addAll(after.keySet());

        for (String key : keys) {

            Object b = before.get(key);
            Object a = after.get(key);

            if (!Objects.equals(b, a)) {

                ChangeItem item = new ChangeItem();

                // ======================================================
                // CASO ESPECIAL: RELATION → VEHICLE (CARD SIMPLE)
                // ======================================================
                if (key.equalsIgnoreCase("relation") && a instanceof Map<?, ?> mapA) {

                    item.setField("Vehicle");

                    if (isCreate) {
                        String modelName = "";
                        String brandName = "";
                        String regionName = "";
                        String typeName = "";

                        // model.modelName
                        Object modelObj = mapA.get("model");
                        if (modelObj instanceof Map<?, ?> modelMap) {
                            Object mn = modelMap.get("modelName");
                            if (mn != null) modelName = mn.toString();
                        }

                        // brandRegionRelation.brand.nameBrand
                        Object brRelObj = mapA.get("brandRegionRelation");
                        if (brRelObj instanceof Map<?, ?> brRelMap) {
                            Object brandObj = brRelMap.get("brand");
                            if (brandObj instanceof Map<?, ?> brandMap) {
                                Object bn = brandMap.get("nameBrand");
                                if (bn != null) brandName = bn.toString();
                            }
                            Object regionObj = brRelMap.get("region");
                            if (regionObj instanceof Map<?, ?> regionMap) {
                                Object rn = regionMap.get("nameRegion");
                                if (rn != null) regionName = rn.toString();
                            }
                        }

                        // type.typeName
                        Object typeObj = mapA.get("type");
                        if (typeObj instanceof Map<?, ?> typeMap) {
                            Object tn = typeMap.get("typeName");
                            if (tn != null) typeName = tn.toString();
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append("Model: ").append(modelName).append("\n");
                        sb.append("Brand: ").append(brandName).append("\n");
                        sb.append("Region: ").append(regionName).append("\n");
                        sb.append("Type: ").append(typeName);

                        item.setBefore("");
                        item.setAfter(sb.toString());
                    } else {
                        item.setBefore(b != null ? b.toString() : "");
                        item.setAfter(a != null ? a.toString() : "");
                    }

                    changes.add(item);
                    continue;
                }

                // ======================================================
                // CAMPOS NORMALES
                // ======================================================
                item.setField(key);

                if (isCreate) {
                    item.setBefore(toPrettyJson(b));
                    item.setAfter(toPrettyJson(a));
                } else {
                    item.setBefore(b != null ? b.toString() : "");
                    item.setAfter(a != null ? a.toString() : "");
                }

                changes.add(item);
            }
        }

        Map<String, Object> out = new HashMap<>();
        out.put("id", req.getId());
        out.put("action", req.getAction());
        out.put("attribute", req.getAttribute());
        out.put("user", req.getUserRequest().getNomUser());
        out.put("created", req.getCreatedAt());
        out.put("changes", changes);
        out.put("pretty", isCreate);

        // ==========================================================
        // AGREGADO: Información completa del vehículo
        // ==========================================================
        VehicleMaster v = req.getVehicle();

        if (v != null) {
            Map<String, Object> vehicle = new HashMap<>();
            vehicle.put("id", v.getIdVehicle());
            vehicle.put("name", v.getNameVehicle());
            vehicle.put("year", v.getYear());
            vehicle.put("fileEng", v.getFileNameEng());
            vehicle.put("fileSpa", v.getFileNameSpa());

            Map<String, Object> relation = new HashMap<>();
            relation.put("model", v.getRelation().getModel().getModelName());
            relation.put("brand", v.getRelation().getBrandRegionRelation().getBrand().getNameBrand());
            relation.put("region", v.getRelation().getBrandRegionRelation().getRegion().getNameRegion());
            relation.put("type", v.getRelation().getType().getTypeName());

            vehicle.put("relation", relation);

            out.put("vehicle", vehicle);
        } else {
            out.put("vehicle", null);
        }

        return out;
    }

    private String toPrettyJson(Object obj) {
        if (obj == null) return "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    // ==========================================================
    //                APPROVE
    // ==========================================================
    @PostMapping("/monitoring/approve/{id}")
    @ResponseBody
    public String approve(@PathVariable Integer id, HttpSession session) throws Exception {
        User approver = (User) session.getAttribute("USER_LOGGED");
        if (approver == null) return "ERROR";
        validationService.approveRequest(id, approver);
        return "OK";
    }

    // ==========================================================
    //                REJECT
    // ==========================================================
    @PostMapping("/monitoring/reject/{id}")
    @ResponseBody
    public String reject(@PathVariable Integer id, HttpSession session) {
        User approver = (User) session.getAttribute("USER_LOGGED");
        if (approver == null) return "ERROR";
        validationService.rejectRequest(id, approver);
        return "OK";
    }
}
