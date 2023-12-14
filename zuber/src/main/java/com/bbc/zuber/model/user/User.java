package com.bbc.zuber.model.user;

import com.bbc.zuber.model.user.enums.Sex;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;
    private UUID uuid;
    private String name;
    private String surname;
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Email(message = "Wrong email pattern. Check it once again!")
    private String email;
    private BigDecimal balance;
}