package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.Size;
import com.bbc.zuber.model.riderequest.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime date;
}
