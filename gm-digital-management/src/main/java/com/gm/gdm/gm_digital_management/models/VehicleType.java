package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_cat_type_vhcl")
@Data
@NoArgsConstructor
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_pk")
    private Integer idType;

    @Column(name = "var_type_name", nullable = false)
    private String typeName;
}