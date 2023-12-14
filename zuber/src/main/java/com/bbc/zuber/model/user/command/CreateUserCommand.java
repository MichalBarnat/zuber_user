package com.bbc.zuber.model.user.command;

import com.bbc.zuber.model.user.enums.Sex;
import com.bbc.zuber.validations.email.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateUserCommand {

    @NotBlank(message = "NAME_NOT_BLANK")
    private String name;
    @NotBlank(message = "SURNAME_NOT_BLANK")
    private String surname;
    //@Past(message = "DOB_CANNOT_BE_FUTURE_OR_PRESENT")
    //@NotNull(message = "DOB_NOT_NULL")
    private String dob;
    @UniqueEmail(message = "GIVEN_EMAIL_EXISTS")
    @Email(message = "INCORRECT_EMAIL_FORMAT")
    @NotBlank(message = "EMAIL_NOT_BLANK")
    private String email;
    @NotNull(message = "SEX_NOT_NULL")
    private Sex sex;
    private BigDecimal balance;
}
