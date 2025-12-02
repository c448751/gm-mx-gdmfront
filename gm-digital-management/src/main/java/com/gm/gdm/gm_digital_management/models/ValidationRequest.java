package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "GDM_MX_PRC_VAL_RQST")
@Data
@NoArgsConstructor
public class ValidationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VALIDATION_PK")
    private Integer id;

    @Column(name = "VAR_ACTION", nullable = false, length = 20)
    private String action;

    @Column(name = "VAR_ATTRIBUTE", nullable = false, length = 50)
    private String attribute;

    @Column(name = "TXT_VALUE_BEFORE", columnDefinition = "TEXT")
    private String valueBefore;

    @Column(name = "TXT_VALUE_AFTER", columnDefinition = "TEXT")
    private String valueAfter;

    @ManyToOne
    @JoinColumn(name = "ID_USER_REQUEST_FK", referencedColumnName = "ID_USER")
    private User userRequest;

    @Column(name = "VAR_STATUS", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "ID_USER_APPROVER_FK", referencedColumnName = "ID_USER")
    private User userApprover;

    @Column(name = "FCH_APPROVAL")
    private LocalDateTime approvalDate;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICLE_FK")
    private VehicleMaster vehicle;

    @Column(name = "FCH_CREATED")
    private LocalDateTime createdAt;
}
