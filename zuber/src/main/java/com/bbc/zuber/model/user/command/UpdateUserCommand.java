package com.bbc.zuber.model.user.command;

import com.bbc.zuber.model.user.enums.Sex;
import lombok.*;

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
}
