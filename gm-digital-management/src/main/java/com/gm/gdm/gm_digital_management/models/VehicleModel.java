package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gdm_mx_cat_model")
@Data
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_model_pk")
    private Integer idModel;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @ManyToOne
    @JoinColumn(
            name = "id_brand_fk",
            referencedColumnName = "id_brand_pk",
            nullable = false
    )
    private Brand brand;

}