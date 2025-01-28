package com.happy.biling.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequestDto {
    private String locationName;
    private Double locationLatitude;
    private Double locationLongitude;
}