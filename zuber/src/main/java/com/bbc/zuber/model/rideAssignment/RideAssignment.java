package com.bbc.zuber.model.rideAssignment;

import com.bbc.zuber.model.rideAssignment.enums.RideAssignmentStatus;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideAssignment {

    private Long id;
    private UUID uuid;
    private UUID rideRequestUUID;
    private UUID driverUUID;
    private String pickUpLocation;
    private String dropOffLocation;
    @Enumerated(STRING)
    private RideAssignmentStatus status;
}
