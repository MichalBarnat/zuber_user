package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateRideRequestCommand {

    private String pickUpLocation;
    private String dropOffLocation;
    private RideRequestType type;
    private RideRequestSize size;
    private LocalDate date;
}
