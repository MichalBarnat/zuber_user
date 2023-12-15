package com.bbc.zuber.model.fundsavailability.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FundsAvailabilityDto {

    private Long id;
    private UUID uuid;
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    private Boolean fundsAvailable;
}
