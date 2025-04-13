package com.d2dapp.backend.dto;

import java.util.List;

public class GeocodingResult {

    private List<GeocodedAddress> successful;
    private List<String> failed;

    public GeocodingResult(List<GeocodedAddress> successful, List<String> failed) {
        this.successful = successful;
        this.failed = failed;
    }

    public List<GeocodedAddress> getSuccessful() {
        return successful;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setSuccessful(List<GeocodedAddress> successful) {
        this.successful = successful;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }
}