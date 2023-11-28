package com.bbc.zuber.model.user.dto;

import com.bbc.zuber.model.user.enums.Sex;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserDto {
    Long id;
    UUID uuid;
    String name;
    Sex sex;
}
