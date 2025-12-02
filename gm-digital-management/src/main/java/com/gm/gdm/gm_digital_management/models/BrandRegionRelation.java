package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_rel_brand_rgn")
@Data
@NoArgsConstructor
public class BrandRegionRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rel_br_rgn_pk")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_brand_fk", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "id_regn_fk", nullable = false)
    private Region region;
}