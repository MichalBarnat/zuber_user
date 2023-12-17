package com.bbc.zuber.model.riderequest;
import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "ride_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_requests_seq")
    @SequenceGenerator(name = "ride_requests_seq", sequenceName = "ride_requests_seq", allocationSize = 1)
    private Long id;
    private UUID uuid;
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    @Enumerated(EnumType.STRING)
    private RideRequestType type;
    @Enumerated(EnumType.STRING)
    private RideRequestSize size;
    private LocalDate date;

    //todo zmienic date na localdate
}
