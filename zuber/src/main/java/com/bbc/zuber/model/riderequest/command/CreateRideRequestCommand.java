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
    //todo wywalic userUuid bo i tak nie jest uzywane
    private UUID userUuid;
    private String pickUpLocation;
    private String dropOffLocation;
    private Type type;
    private Size size;
    private String date;
}
