package com.bonifacio.urls_ripper.mappers;

import com.bonifacio.urls_ripper.dtos.UrlDto;
import com.bonifacio.urls_ripper.dtos.UrlUserDto;
import com.bonifacio.urls_ripper.entities.Url;
import com.bonifacio.urls_ripper.entities.UserUrl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UrlMapper {
    UrlMapper INSTANCE = Mappers.getMapper(UrlMapper.class);
    Url urlDtoToUrl(UrlDto urlDto);
    UserUrl urlUserDtoToUserUrl(UrlUserDto urlUserDto);
}
