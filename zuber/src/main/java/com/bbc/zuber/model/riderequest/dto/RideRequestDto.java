package com.bbc.zuber.model.riderequest.dto;

import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class RideRequestDto {
    Long id;
    UUID uuid;
    UUID userUuid;
    String pickUpLocation;
    String dropOffLocation;
    RideRequestType type;
    RideRequestSize size;
    LocalDate date;
}