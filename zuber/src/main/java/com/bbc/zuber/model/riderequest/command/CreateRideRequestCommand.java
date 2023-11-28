package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateRideRequestCommand {
    private UUID userId;
    private String pickUpLocation;
    private String dropOffLocation;
    private Type type;
    private Size size;
    private String date;
}
