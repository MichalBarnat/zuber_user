package com.bbc.zuber.model.ridecancel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "ride_cancelled")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UUID rideAssignmentId;
    private Boolean cancel;

}
