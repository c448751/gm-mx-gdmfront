package com.gm.gdm.gm_digital_management.dto;


import lombok.Data;

// DTO solo para el formulario de Management-Add
@Data
public class VehicleFormDTO {

    private Integer regionId;
    private Integer brandId;
    private Integer typeId;
    private Integer modelId;

    private String nameVehicle;
    private Integer year;
}