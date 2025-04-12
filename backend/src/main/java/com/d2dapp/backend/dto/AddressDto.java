package com.d2dapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String ulica;
    private String hisnaStevilka;
    private String dodatek;
    private String obcina;
    private String tehnologija;
}