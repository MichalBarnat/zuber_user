package com.bbc.zuber.mapping.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.UpdateUserCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UpdateUserCommandToUserConverter implements Converter<UpdateUserCommand, User> {

    @Override
    public User convert(MappingContext<UpdateUserCommand, User> mappingContext) {
        UpdateUserCommand command = mappingContext.getSource();

        return User.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .dob(LocalDate.parse(command.getDob()))
                .sex(command.getSex())
                .email(command.getEmail())
                .build();

    }
}
