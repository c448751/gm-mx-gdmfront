package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "gdm_mx_log_changes")
@Data
@NoArgsConstructor
public class LogChanges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log_pk")
    private Integer id;

    @Column(name = "var_action", nullable = false, length = 20)
    private String action;  // INSERT, UPDATE, DELETE

    @Column(name = "var_user", nullable = false, length = 100)
    private String user;

    @Column(name = "var_detail", columnDefinition = "TEXT")
    private String detail;

    @Column(name = "fch_log", nullable = false)
    private LocalDateTime date;
}