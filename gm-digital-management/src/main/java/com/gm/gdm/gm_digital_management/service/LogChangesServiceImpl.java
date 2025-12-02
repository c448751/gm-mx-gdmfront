package com.gm.gdm.gm_digital_management.service;


import com.gm.gdm.gm_digital_management.models.LogChanges;

import com.gm.gdm.gm_digital_management.respository.LogChangesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogChangesServiceImpl implements LogChangesService {

    private final LogChangesRepository repository;

    public LogChangesServiceImpl(LogChangesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(String action, String user, String detail) {
        LogChanges log = new LogChanges();
        log.setAction(action);
        log.setUser(user);
        log.setDetail(detail);
        log.setDate(LocalDateTime.now());
        repository.save(log);
    }
}