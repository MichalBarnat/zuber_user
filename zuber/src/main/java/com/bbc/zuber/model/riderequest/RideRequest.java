package com.bbc.zuber.model.riderequest;
import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

@Entity(name = "ride_requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private UUID uuid;
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    @Enumerated(EnumType.STRING)
    private RideRequestType type;
    @Enumerated(EnumType.STRING)
    private RideRequestSize size;
    private String date;

    //todo zmienic date na localdate
}
