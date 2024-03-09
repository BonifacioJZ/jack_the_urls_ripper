package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperImplements implements UserMapper {
    @Override
    public User registerDtoToUser(RegisterDto registerDto) {
        if(registerDto == null){
            return null;
        }
        return User.builder()
                .username(registerDto.username())
                .email(registerDto.email())
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .build();
    }
}
