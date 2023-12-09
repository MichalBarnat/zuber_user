package com.bbc.zuber.model.fundsavailability;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "funds_availability")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID uuid;
    private String pickUpLocation;
    private String dropOffLocation;
    private boolean fundsAvailable;

}
