package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    @Override
    public UserDetails userToUserDetails(User user,List<UrlsDetails> urlsDetailsList) {
        if(user == null){
            return null;
        }
        return UserDetails
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .urls(urlsDetailsList)
                .build();
    }

    @Override
    public UserDetails userToUserDetails(Optional<User> userOptional, List<UrlsDetails> urlsDetailsList) {
        return userOptional.map(user -> UserDetails
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .urls(urlsDetailsList)
                .build()).orElse(null);
    }
}
