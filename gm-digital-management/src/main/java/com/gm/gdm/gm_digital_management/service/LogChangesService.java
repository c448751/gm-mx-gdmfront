package com.gm.gdm.gm_digital_management.service;

import com.gm.gdm.gm_digital_management.models.LogChanges;

public interface LogChangesService {
    void save(String action, String user, String detail);
}