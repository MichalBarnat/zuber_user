package com.bbc.zuber.mapping.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.CreateUserCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
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
                .balance(command.getBalance())
                .isDeleted(false)
                .build();
    }
}
