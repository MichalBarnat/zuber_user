package com.bbc.zuber.model.riderequest.dto;

import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class RideRequestDto {

    Long id;
    UUID uuid;
    UUID userId;
    String pickUpLocation;
    String dropOffLocation;
    RideRequestType type;
    RideRequestSize size;
    String date;
}