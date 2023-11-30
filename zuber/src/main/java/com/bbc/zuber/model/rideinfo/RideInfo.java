package com.bbc.zuber.model.rideinfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "ride_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userUuid;
    private UUID driverUuid;
    private String driverName;
    private String driverLocation;
}
