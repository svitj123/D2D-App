package com.d2dapp.backend.controller;

import com.d2dapp.backend.entity.GeocodedAddress;
import com.d2dapp.backend.service.GeoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    @Autowired
    private GeoService geoService;

    @GetMapping("/coords")
    public ResponseEntity<GeocodedAddress> getCoords(@RequestParam String address) {
        GeocodedAddress coords = geoService.getCoordinates(address);
        if (coords != null) {
            return ResponseEntity.ok(coords);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
    
    @PostMapping("/batch/coords")
    public List<GeocodedAddress> getBatchCoords(@RequestBody List<String> addresses) {
        return geoService.getCoordinatesForAddresses(addresses);
    }
}