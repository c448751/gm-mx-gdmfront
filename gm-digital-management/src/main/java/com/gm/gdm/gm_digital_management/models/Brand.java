package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_cat_brand")
@Data
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_brand_pk")
    private Integer idBrand;

    @Column(name = "var_name", nullable = false)
    private String nameBrand;
}