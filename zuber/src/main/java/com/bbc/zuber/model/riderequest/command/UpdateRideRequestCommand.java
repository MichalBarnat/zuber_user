package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
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
