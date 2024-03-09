package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.dtos.UrlsDetails;
import com.bonifacio.urls_ripper.dtos.UserDetails;
import com.bonifacio.urls_ripper.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerDtoToUser(RegisterDto registerDto);
    UserDetails userToUserDetails(User user, List<UrlsDetails> urlsDetailsList);
    UserDetails userToUserDetails(Optional<User> userOptional,List<UrlsDetails> urlsDetailsList );
}
