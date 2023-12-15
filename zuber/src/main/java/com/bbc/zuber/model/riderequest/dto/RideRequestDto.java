package com.bbc.zuber.model.riderequest.dto;

import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
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
    Type type;
    Size size;
    LocalDateTime date;
}