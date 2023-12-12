package com.bbc.zuber.model.user.command;

import com.bbc.zuber.model.user.enums.Sex;
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
    private String email;
    private Sex sex;
    private BigDecimal balance;
}
