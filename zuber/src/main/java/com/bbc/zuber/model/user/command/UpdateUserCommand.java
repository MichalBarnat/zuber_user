package com.bbc.zuber.model.user.command;

import com.bbc.zuber.model.user.enums.Sex;
import com.bbc.zuber.validations.email.UniqueEmail;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserCommand {

    private String name;
    private String surname;
    private String dob;
    @UniqueEmail(message = "GIVEN_EMAIL_EXISTS")
    @Email(message = "INCORRECT_EMAIL_FORMAT")
    private String email;
    private Sex sex;
    private BigDecimal balance;
}
