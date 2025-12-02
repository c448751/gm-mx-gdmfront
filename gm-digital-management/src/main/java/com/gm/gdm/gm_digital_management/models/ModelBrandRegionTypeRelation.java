package com.gm.gdm.gm_digital_management.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_rel_brgn_type_modelrgn")
@Data
@NoArgsConstructor
public class ModelBrandRegionTypeRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rel_brgn_type_model_pk")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_model_fk", nullable = false)
    private VehicleModel model;

    @ManyToOne
    @JoinColumn(name = "id_rel_br_rgn_fk", nullable = false)
    private BrandRegionRelation brandRegionRelation;

    @ManyToOne
    @JoinColumn(name = "id_type_fk", nullable = false)
    private VehicleType type;
}