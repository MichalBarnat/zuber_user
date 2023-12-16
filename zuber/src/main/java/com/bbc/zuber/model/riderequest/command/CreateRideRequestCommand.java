package com.bbc.zuber.model.riderequest.command;

import com.bbc.zuber.model.riderequest.enums.RideRequestSize;
import com.bbc.zuber.model.riderequest.enums.RideRequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateRideRequestCommand {
    @NotBlank(message = "PICK_UP_LOCATION_NOT_BLANK")
    private String pickUpLocation;
    @NotBlank(message = "DROP_OFF_LOCATION_NOT_BLANK")
    private String dropOffLocation;
    @NotNull(message = "TYPE_NOT_NULL")
    private RideRequestType type;
    @NotNull(message = "SIZE_NOT_NULL")
    private RideRequestSize size;
    // walidacja dla daty jak polem bedzie LocalDateTime
    // @Future(message = "DATE_CANNOT_BE_PAST_OR_PRESENT")
    // @NotNull(message = "DATE_NOT_NULL")
    private String date;
}
