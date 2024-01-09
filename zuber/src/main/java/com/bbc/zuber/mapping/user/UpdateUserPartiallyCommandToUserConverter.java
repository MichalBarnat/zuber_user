package com.bbc.zuber.mapping.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.command.UpdateUserPartiallyCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserPartiallyCommandToUserConverter implements Converter<UpdateUserPartiallyCommand, User> {

    @Override
    public User convert(MappingContext<UpdateUserPartiallyCommand, User> mappingContext) {
        UpdateUserPartiallyCommand command = mappingContext.getSource();

        return User.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .dob(command.getDob())
                .sex(command.getSex())
                .email(command.getEmail())
                .balance(command.getBalance())
                .build();

    }
}
