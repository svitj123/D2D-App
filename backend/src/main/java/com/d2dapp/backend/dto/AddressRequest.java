package com.d2dapp.backend.dto;

public class AddressRequest {
    private String fullAddress;
    private String tehnologija;

    // Konstruktorji
    public AddressRequest() {}
    public AddressRequest(String fullAddress, String tehnologija) {
        this.fullAddress = fullAddress;
        this.tehnologija = tehnologija;
    }

    // Getterji in setterji
    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getTehnologija() {
        return tehnologija;
    }

    public void setTehnologija(String tehnologija) {
        this.tehnologija = tehnologija;
    }
}