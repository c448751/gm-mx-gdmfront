package com.gm.gdm.gm_digital_management.controller;

import com.gm.gdm.gm_digital_management.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String name,
            Model m
    ) {
        m.addAttribute("results", searchService.search(region, brand, type, model, year, name));
        return "home/main";
    }
}