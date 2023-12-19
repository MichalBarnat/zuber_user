package com.bbc.zuber.model.fundsavailability;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "funds_availability")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsAvailability {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private UUID uuid;
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    private Boolean fundsAvailable;
}
