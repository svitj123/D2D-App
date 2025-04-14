package com.d2dapp.backend.service;

import com.d2dapp.backend.dto.GeocodedAddress;
import com.d2dapp.backend.repository.GeocodedAddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            // Dodaj Slovenija na konec (če še ni)
            if (!fullAddress.toLowerCase().contains("slovenija")) {
                fullAddress += ", Slovenija";
            }

            String encoded = URLEncoder.encode(fullAddress, StandardCharsets.UTF_8);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encoded + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            var json = new ObjectMapper().readTree(response.getBody());

            if (json.at("/results").isEmpty()) return Optional.empty();

            var result = json.at("/results/0");
            var location = result.at("/geometry/location");
            var locationType = result.at("/geometry/location_type").asText();
            var partialMatch = result.has("partial_match") && result.get("partial_match").asBoolean();

            // Sprejmemo le natančne lokacije
            if (!"ROOFTOP".equals(locationType) || partialMatch) return Optional.empty();

            double lat = location.get("lat").asDouble();
            double lng = location.get("lng").asDouble();

            GeocodedAddress resultObj = new GeocodedAddress(fullAddress, lat, lng, null, "geocoded");
            repository.save(resultObj);
            return Optional.of(resultObj);

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
