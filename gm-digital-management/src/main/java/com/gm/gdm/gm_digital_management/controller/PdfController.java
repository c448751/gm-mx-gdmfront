package com.gm.gdm.gm_digital_management.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;

@Controller
public class PdfController {

    private final String pdfPath = "C:/gdm/pdf/";

    @GetMapping("/pdf/{filename}")
    public ResponseEntity<Resource> openPdf(@PathVariable("filename") String filename) {

        try {
            File file = new File(pdfPath + filename);

            if (!file.exists()) {
                System.out.println("❌ PDF NO ENCONTRADO: " + file.getAbsolutePath());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            System.out.println("❌ ERROR AL ABRIR PDF: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
