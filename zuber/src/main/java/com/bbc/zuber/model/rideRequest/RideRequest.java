package com.bbc.zuber.model.rideRequest;

import com.bbc.zuber.model.rideRequest.enums.Size;
import com.bbc.zuber.model.rideRequest.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "rideRequests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private String pickUpLocation;
    private String dropOffLocation;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Size size;
    private String date;
}
