package com.bbc.zuber.model.rideRequest.command;

import com.bbc.zuber.model.rideRequest.enums.Size;
import com.bbc.zuber.model.rideRequest.enums.Type;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateRideRequestCommand {
    private String pickUpLocation;
    private String dropOffLocation;
    private Type type;
    private Size size;
    private String date;
}
