package com.gm.gdm.gm_digital_management.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GDM_MX_MAE_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private Integer idUser;   // ← CAMBIADO A Integer

    @Column(name = "COD_USER")
    private String codUser;

    @Column(name = "USR_FULL_NAME")
    private String nomUser;

    @Column(name = "VAR_ROL")
    private String rolUser;

    @Column(name = "ID_REGN_FK")
    private Integer regionUser;  // ← CAMBIADO A Integer

    @Column(name = "FLG_STATS")
    private String flagUser;

    @Column(name = "FCH_CRT")
    private Date createDateUser;

    @Column(name = "FCH_UPT")
    private Date updateDateUser;

    // GETTERS & SETTERS
    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getCodUser() {
        return codUser;
    }

    public void setCodUser(String codUser) {
        this.codUser = codUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getRolUser() {
        return rolUser;
    }

    public void setRolUser(String rolUser) {
        this.rolUser = rolUser;
    }

    public Integer getRegionUser() {
        return regionUser;
    }

    public void setRegionUser(Integer regionUser) {
        this.regionUser = regionUser;
    }

    public String getFlagUser() {
        return flagUser;
    }

    public void setFlagUser(String flagUser) {
        this.flagUser = flagUser;
    }

    public Date getCreateDateUser() {
        return createDateUser;
    }

    public void setCreateDateUser(Date createDateUser) {
        this.createDateUser = createDateUser;
    }

    public Date getUpdateDateUser() {
        return updateDateUser;
    }

    public void setUpdateDateUser(Date updateDateUser) {
        this.updateDateUser = updateDateUser;
    }
}
