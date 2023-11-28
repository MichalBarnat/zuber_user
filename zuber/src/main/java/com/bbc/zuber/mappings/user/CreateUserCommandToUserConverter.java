package com.bbc.zuber.mappings.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.UUID;

public class CreateUserCommandToUserConverter implements Converter<CreateUserCommand, User> {

    @Override
    public User convert(MappingContext<CreateUserCommand, User> mappingContext) {
        CreateUserCommand command = mappingContext.getSource();

        return User.builder()
                .uuid(UUID.randomUUID())
                .name(command.getName())
                .surname(command.getSurname())
                .dob(command.getDob())
                .sex(command.getSex())
                .email(command.getEmail())
                .build();
    }
}
