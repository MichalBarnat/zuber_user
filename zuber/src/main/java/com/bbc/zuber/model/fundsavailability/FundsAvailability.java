package com.bbc.zuber.model.fundsavailability;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funds_availability_seq")
    @SequenceGenerator(name = "funds_availability_seq", sequenceName = "funds_availability_seq", allocationSize = 1)
    private Long id;
    private UUID uuid;
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    private Boolean fundsAvailable;
}
