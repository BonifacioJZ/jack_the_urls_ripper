package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.RegisterDto;
import com.bonifacio.urls_ripper.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerDtoToUser(RegisterDto registerDto);
}
