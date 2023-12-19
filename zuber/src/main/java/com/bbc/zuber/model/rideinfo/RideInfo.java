package com.bbc.zuber.model.rideinfo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "ride_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_info_seq")
    @SequenceGenerator(name = "ride_info_seq", sequenceName = "ride_info_seq", allocationSize = 1)
    private Long id;
    @Column(name = "ride_assignment_uuid")
    private UUID rideAssignmentUuid;
    @Column(name = "user_uuid")
    private UUID userUuid;
    @Column(name = "driver_uuid")
    private UUID driverUuid;
    @Column(name = "driver_name")
    private String driverName;
    @Column(name = "driver_location")
    private String driverLocation;
    @Column(name = "pick_up_location")
    private String pickUpLocation;
    @Column(name = "drop_up_location")
    private String dropUpLocation;
    @Column(name = "order_time")
    private LocalDateTime orderTime;
    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;
    @Column(name = "cost_of_ride")
    private BigDecimal costOfRide;
    @Column(name = "time_to_arrival_in_minutes")
    private String timeToArrivalInMinutes;
    @Column(name = "ride_length_in_kilometers")
    private String rideLengthInKilometers;
}
