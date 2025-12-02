package com.gm.gdm.gm_digital_management.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_mae_vhcl")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vhcl_pk")
    private Integer idVehicle;

    @ManyToOne
    @JoinColumn(name = "id_rel_brgn_type_model_fk", nullable = false)
    private ModelBrandRegionTypeRelation relation;

    @Column(name = "var_eng_file_name")
    private String fileNameEng;

    @Column(name = "var_spa_file_name")
    private String fileNameSpa;

    @Column(name = "var_vhcl_year", nullable = false)
    private Integer year;

    @Column(name = "var_vhcl_name", nullable = false)
    private String nameVehicle;

    @Column(name = "var_status")
    private String status;

    public Integer getIdVehicle() {
        return idVehicle;
    }
}