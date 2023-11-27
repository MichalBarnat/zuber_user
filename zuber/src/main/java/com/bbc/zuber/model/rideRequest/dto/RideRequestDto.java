package com.bbc.zuber.model.rideRequest.dto;

import com.bbc.zuber.model.rideRequest.enums.Size;
import com.bbc.zuber.model.rideRequest.enums.Type;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class RideRequestDto {
    Long id;
    UUID uuid;
    String pickUpLocation;
    String dropOffLocation;
    Type type;
    Size size;
    String date;
}