package com.d2dapp.backend.controller;

import com.d2dapp.backend.dto.AddressDto;
import com.d2dapp.backend.service.ExcelReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // omogočiš frontend dostop
@RequestMapping("/api")
public class UploadController {

    @Autowired
    private ExcelReaderService excelReaderService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Napaka: Datoteka je prazna.");
        }

        try {
            List<AddressDto> addresses = excelReaderService.readAddressesFromExcel(file);
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Napaka pri branju Excel datoteke: " + e.getMessage());
        }
    }
}