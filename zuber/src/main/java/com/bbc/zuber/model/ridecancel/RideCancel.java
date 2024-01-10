package com.bbc.zuber.model.ridecancel;

import jakarta.persistence.Column;
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

@Entity(name = "ride_cancelled")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_cancel_seq")
    @SequenceGenerator(name = "ride_cancel_seq", sequenceName = "ride_cancel_seq", allocationSize = 1)
    private Long id;
    @Column(name = "ride_assignment_uuid")
    private UUID rideAssignmentUuid;
}
