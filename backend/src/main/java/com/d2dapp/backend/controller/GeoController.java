package com.d2dapp.backend.controller;

import com.d2dapp.backend.dto.GeocodedAddress;
import com.d2dapp.backend.dto.GeocodingResult;
import com.d2dapp.backend.service.ExcelGeocodingService;
import com.d2dapp.backend.service.GeoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    @Autowired
    private GeoService geoService;

    @Autowired
    private ExcelGeocodingService excelService;

    @GetMapping("/coords")
    public ResponseEntity<GeocodedAddress> getCoords(@RequestParam String address) {
        GeocodedAddress coords = geoService.getCoordinates(address);
        return coords != null
            ? ResponseEntity.ok(coords)
            : ResponseEntity.notFound().build();
    }

    @PostMapping("/batch/coords")
    public ResponseEntity<List<GeocodedAddress>> getBatchCoords(@RequestBody List<String> addresses) {
        List<GeocodedAddress> result = geoService.getCoordinatesForAddresses(addresses);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/batch")
    public ResponseEntity<GeocodingResult> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            GeocodingResult result = excelService.processExcelFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}   