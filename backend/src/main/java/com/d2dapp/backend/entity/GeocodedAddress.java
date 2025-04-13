package com.d2dapp.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "geocoded_addresses")
public class GeocodedAddress {

    @Id
    private String id;

    private String fullAddress;
    private double lat;
    private double lng;
    private String tehnologija;

    public GeocodedAddress() {}

    public GeocodedAddress(String fullAddress, double lat, double lng) {
        this.fullAddress = fullAddress;
        this.lat = lat;
        this.lng = lng;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTehnologija() {
        return tehnologija;
    }

    public void setTehnologija(String tehnologija) {
        this.tehnologija = tehnologija;
    }
}