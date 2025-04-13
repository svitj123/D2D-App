package com.d2dapp.backend.dto;

public class GeocodedAddress {
    private String fullAddress;
    private double lat;
    private double lng;
    private String tehnologija;
    private String status;

    // Konstruktorji
    public GeocodedAddress() {}

    public GeocodedAddress(String fullAddress, double lat, double lng, String tehnologija, String status) {
        this.fullAddress = fullAddress;
        this.lat = lat;
        this.lng = lng;
        this.tehnologija = tehnologija;
        this.status = status;
    }

    // Getterji in setterji
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}