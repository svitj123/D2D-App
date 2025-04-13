package com.d2dapp.backend.service;

import com.d2dapp.backend.dto.GeocodedAddress;
import com.d2dapp.backend.repository.GeocodedAddressRepository;

import ch.qos.logback.core.net.SyslogOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GeoService {

    private final GeocodedAddressRepository repository;

    @Value("${google.maps.api.key}")
    private String apiKey;

    public GeoService(GeocodedAddressRepository repository) {
        this.repository = repository;
    }

    public GeocodedAddress getCoordinates(String fullAddress) {
        Optional<GeocodedAddress> cached = repository.findByFullAddress(fullAddress);
        if (cached.isPresent()) {
            GeocodedAddress addr = cached.get();
            addr.setStatus("cached");
            return addr;
        }

        return geocodeAndSave(fullAddress).orElseThrow(() ->
            new RuntimeException("Naslova ni bilo mogoče geokodirati: " + fullAddress)
        );
    }

    public Optional<GeocodedAddress> geocodeAndSave(String fullAddress) {
        try {
            String encoded = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encoded + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            var json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.getBody());
            var location = json.at("/results/0/geometry/location");

            if (location.isMissingNode()) return Optional.empty();

            double lat = location.get("lat").asDouble();
            double lng = location.get("lng").asDouble();

            GeocodedAddress result = new GeocodedAddress(fullAddress, lat, lng, null, "geocoded");
            repository.save(result);
            return Optional.of(result);

        } catch (Exception e) {
            System.err.println("Napaka pri geokodiranju: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<GeocodedAddress> getCoordinatesForAddresses(List<String> addresses) {
        List<GeocodedAddress> results = new ArrayList<>();

        for (String address : addresses) {
            GeocodedAddress resolved = getCoordinates(address);
            results.add(resolved);
        }

        return results;
    }
}
