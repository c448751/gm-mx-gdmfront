package com.gm.gdm.gm_digital_management.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_cat_rgn")
@Data
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regn_pk")
    private Integer idRegion;

    @Column(name = "var_name", nullable = false)
    private String nameRegion;
}
