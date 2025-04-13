package com.d2dapp.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.d2dapp.backend.dto.GeocodedAddress;

import java.util.Optional;

public interface GeocodedAddressRepository extends MongoRepository<GeocodedAddress, String> {
    Optional<GeocodedAddress> findByFullAddress(String fullAddress);
}