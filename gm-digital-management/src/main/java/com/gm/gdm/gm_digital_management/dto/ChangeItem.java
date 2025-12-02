package com.gm.gdm.gm_digital_management.dto;

import lombok.Data;

@Data
public class ChangeItem {
    private String field;
    private String before;
    private String after;
}