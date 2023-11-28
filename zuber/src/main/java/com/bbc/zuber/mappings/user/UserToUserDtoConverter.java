package com.bbc.zuber.mappings.user;

import com.bbc.zuber.model.user.User;
import com.bbc.zuber.model.user.dto.UserDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(MappingContext<User, UserDto> mappingContext) {
        User user = mappingContext.getSource();

        return UserDto.builder()
                .id(user.getId())
                .uuid(user.getUuid())
                .name(user.getName())
                .sex(user.getSex())
                .build();
    }
}
